package de.explore.grabby.booking.service;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.model.entity.embedded.Image;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import de.explore.grabby.booking.rest.request.UploadForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@ApplicationScoped
public class BookingEntityService {

  public static final String DEFAULT_ENTITY_IMAGE_PNG = "default-entity-image.png";

  @Inject
  FileService fileService;

  @Inject
  BookingEntityRepository bookingEntityRepository;

  @ConfigProperty(name = "bucket.name")
  String bucket;

  @Transactional
  public void uploadImageForEntity(Long id, UploadForm form) {
    BookingEntity bookingEntity = bookingEntityRepository.findByIdOptional(id).orElseThrow();
    String filename = fileService.uploadImage(bucket, form);

    Image image = createImageObject(filename);

    bookingEntity.setImage(image);
    bookingEntityRepository.persist(bookingEntity);
  }

  private Image createImageObject(String filename) {
    Image image = new Image();
    image.setBucket(bucket);
    image.setFilename(filename);
    return image;
  }

  public ResponseInputStream<GetObjectResponse> getImageForEntity(long id) {
    BookingEntity bookingEntity = bookingEntityRepository.findByIdOptional(id).orElseThrow();
    if (bookingEntity.getImage() == null) {
      return null;
    }
    return fileService.getImage(bookingEntity.getImage().getBucket(), bookingEntity.getImage().getFilename());
  }

  public ResponseInputStream<GetObjectResponse> getDefaultEntityImage() {
    return fileService.getImage(bucket, DEFAULT_ENTITY_IMAGE_PNG);
  }
}
