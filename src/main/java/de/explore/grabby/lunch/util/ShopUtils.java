package de.explore.grabby.lunch.util;

import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.repository.ShopRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.Optional;


public class ShopUtils {

  @Inject
  static ShopRepository shopRepository;

  private ShopUtils() {
  }

  public static void ensureShopByIdExists(long id) {
    Optional<Shop> byIdOptional = shopRepository.findByIdOptional(id);
    if (byIdOptional.isEmpty()) {
      throw new NotFoundException("Could not find shop by id " + id);
    }
  }
}
