package de.explore.grabby.lunch.service;


import de.explore.grabby.booking.model.entity.embedded.Image;
import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.repository.ShopRepository;
import de.explore.grabby.lunch.rest.request.UploadForm;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public class ShopService {

  public static final String DEFAULT_SHOP_IMAGE_PNG = "default-shop-image.png";

  private static final Logger LOG = LoggerFactory.getLogger(ShopService.class);

  @Inject
  FileService fileService;

  @Inject
  ShopRepository shopRepository;

  @ConfigProperty(name = "bucket.name")
  String bucket;

  @Transactional
  public void uploadImageForEntity(Long id, UploadForm form) {
    Shop shop = shopRepository.findByIdOptional(id).orElseThrow();
    String filename = fileService.uploadImage(bucket, form);
    Image image = createImageObject(filename);

    shop.setImage(image);
    shopRepository.persist(shop);
    LOG.info("Image was uploaded for shop with id {}", id);
  }

  private Image createImageObject(String filename) {
    Image image = new Image();
    image.setBucket(bucket);
    image.setFilename(filename);
    return image;
  }

  public ResponseInputStream<GetObjectResponse> getImageForEntity(long id) {
    Shop shop = shopRepository.findByIdOptional(id).orElseThrow();
    if (shop.getImage() == null) {
      return null;
    }
    return fileService.getImage(shop.getImage().getBucket(), shop.getImage().getFilename());
  }

  public ResponseInputStream<GetObjectResponse> getDefaultEntityImage() {
    return fileService.getImage(bucket, DEFAULT_SHOP_IMAGE_PNG);
  }
}
