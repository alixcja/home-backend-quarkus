package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.Favorite;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class FavoriteRepository implements PanacheRepository<Favorite> {

  public void addNewFavorite(Favorite favorite) {
    Optional<Favorite> favoriteToPersist = find("favorite", favorite.getFavorite()).singleResultOptional();
    if (favoriteToPersist.isEmpty()) {
      persist(favorite);
    }
  }
}
