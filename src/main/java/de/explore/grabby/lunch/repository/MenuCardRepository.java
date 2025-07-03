package de.explore.grabby.lunch.repository;

import de.explore.grabby.fileservice.FileService;
import de.explore.grabby.lunch.model.MenuCard;
import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.rest.request.MenuUploadForm;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ApplicationScoped
public class MenuCardRepository implements PanacheRepository<MenuCard> {

  @Inject
  FileService fileService;

  @Inject
  ShopRepository shopRepository;

  @ConfigProperty(name = "menuCard.bucket.name")
  String bucket;

  public void handleMenuCard(Long shopId, MenuUploadForm newMenuCard) {
    Optional<MenuCard> optionalMenuCard = findByNumberOptional(newMenuCard.number);
    if (optionalMenuCard.isPresent()) {
      // TODO: Remove old one - if new menuCardSize is smaller then old ones - old ones needs to be deleted too
      String fileName = upload(newMenuCard);
      updateFileName(newMenuCard.number, fileName);
    } else {
      uploadAndPersist(shopId, newMenuCard);
    }
  }

  private Optional<MenuCard> findByNumberOptional(int number) {
    return find("number = ?1", number).stream().findAny();
  }

  private void uploadAndPersist(Long shopId, MenuUploadForm menuCard) {
    Shop shopById = shopRepository.findById(shopId);
    String fileName = upload(menuCard);
    persist(fileName, shopById, menuCard.number);
  }

  private String upload(MenuUploadForm menuCard) {
    System.out.println("filesize " + menuCard.file.length());
    return fileService.uploadImage(bucket, menuCard);
  }

  @Transactional
  public void persist(String fileName, Shop shop, int number) {
    MenuCard newMenuCard = new MenuCard();
    newMenuCard.setFileName(fileName);
    newMenuCard.setShop(shop);
    newMenuCard.setNumber(number);

    persist(newMenuCard);
  }

  @Transactional
  public void updateFileName(int menuCardNumber, String fileName) {
    MenuCard toUpdate = findByNumberOptional(menuCardNumber).orElseThrow();
    toUpdate.setFileName(fileName);
    persist(toUpdate);
  }

  public Optional<MenuCard> findByShopAndNumber(Long id, Long number) {
    return find("shop.id = ?1 and number = ?2", id, number).list().stream().findFirst();
  }
}
