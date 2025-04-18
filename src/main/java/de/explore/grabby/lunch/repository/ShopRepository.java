package de.explore.grabby.lunch.repository;

import de.explore.grabby.lunch.model.Shop;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class ShopRepository implements PanacheRepository<Shop> {

  @Transactional
  public Shop put(long id, @Valid @NotNull Shop updatedShop) {
    Shop shopToUpdate = findById(id);

    shopToUpdate.setName(updatedShop.getName());
    shopToUpdate.setWebsite(updatedShop.getWebsite());
    shopToUpdate.setPhoneNumber(updatedShop.getPhoneNumber());
    shopToUpdate.setAddress(updatedShop.getAddress());
    shopToUpdate.getOpeningHours().addAll(updatedShop.getOpeningHours());
    shopToUpdate.setArchived(updatedShop.getArchived());

    persist(shopToUpdate);
    return shopToUpdate;
  }
}
