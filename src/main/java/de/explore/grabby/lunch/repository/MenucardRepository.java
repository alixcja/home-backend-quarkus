package de.explore.grabby.lunch.repository;

import de.explore.grabby.lunch.model.Menucard;
import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.rest.request.UploadForm;
import de.explore.grabby.lunch.service.FileService;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MenucardRepository implements PanacheRepository<Menucard> {

  @Inject
  FileService fileService;

  @Inject
  ShopRepository shopRepository;

  @ConfigProperty(name = "shop.bucket.name")
  String bucket;

  public void uploadMenucards(Long shopId, List<UploadForm> menucards) {
    List<Menucard> allMenucardsByShop = findAllMenucardsByShop(shopId);
    Shop shopById = shopRepository.findById(shopId);
    menucards.forEach(card -> uploadAndPersist(card, allMenucardsByShop, shopById));
  }

  private void uploadAndPersist(UploadForm menucard, List<Menucard> findByShopId, Shop shopById) {
    String fileName = fileService.uploadImage(bucket, menucard);
    Optional<Menucard> byNumber = findByShopId.stream().filter(card -> card.getNumber() == menucard.number).findFirst();

    if (byNumber.isEmpty()) {
      perist(menucard, fileName, shopById);
    } else {
      update(byNumber.get(), fileName);
    }
  }

  private void update(Menucard byNumber, String fileName) {
    byNumber.setFileName(fileName);
    persist(byNumber);
  }

  private void perist(UploadForm menucard, String fileName, Shop shopById) {
    Menucard newMenucard = new Menucard();
    newMenucard.setNumber(menucard.number);
    newMenucard.setFileName(fileName);
    newMenucard.setShop(shopById);
    persist(newMenucard);
  }

  public List<Menucard> findAllMenucardsByShop(Long id) {
    return find("shop.id", id).list().stream()
            .sorted(Comparator.comparingInt(Menucard::getNumber))
            .toList();
  }
}
