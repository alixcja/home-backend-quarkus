package de.explore.grabby.lunch.repository;

import de.explore.grabby.lunch.model.MenuCard;
import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.rest.request.UploadForm;
import de.explore.grabby.lunch.service.FileService;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class MenuCardRepository implements PanacheRepository<MenuCard> {

  @Inject
  FileService fileService;

  @Inject
  ShopRepository shopRepository;

  @ConfigProperty(name = "shop.bucket.name")
  String bucket;

  public void uploadAndPersist(Long shopId, UploadForm menuCard) {
    Shop shopById = shopRepository.findById(shopId);
    String fileName = upload(menuCard);
    persist(fileName, shopById, menuCard.number);
  }

  public void uploadAndUpdate(Long menuCardId, @Valid UploadForm menuCard) {
    String fileName = upload(menuCard);
    uploadAndUpdate(menuCardId, fileName);
  }

  private String upload(UploadForm menuCard) {
    return fileService.uploadImage(bucket, menuCard);
  }

  @Transactional
  public void persist(String fileName, Shop shop, int number ) {
    MenuCard newMenuCard = new MenuCard();
    newMenuCard.setFileName(fileName);
    newMenuCard.setShop(shop);
    newMenuCard.setNumber(number);

    persist(newMenuCard);
  }

  @Transactional
  public void uploadAndUpdate(Long menuCardId, String fileName) {
    MenuCard toUpdate = findById(menuCardId);
    toUpdate.setFileName(fileName);
    persist(toUpdate);
  }

  public List<MenuCard> findAllMenuCardsByShop(Long id) {
    return find("shop.id", id).list().stream()
            .sorted(Comparator.comparingInt(MenuCard::getNumber))
            .toList();
  }
}
