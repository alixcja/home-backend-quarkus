package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.Favorite;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FavoriteRepository implements PanacheRepository<Favorite> {

  @Transactional
  public void addNewFavorite(Favorite favorite, String userId) {
    Optional<Favorite> favoriteToPersist = find("favorite", favorite.getFavorite()).singleResultOptional();
    if (favoriteToPersist.isEmpty()) {
      favorite.setUserId(userId);
      persist(favorite);
    }
  }

  @Transactional
  public void delete(long favoriteId) {
    deleteById(favoriteId);
  }

  public List<Favorite> listAllFavorites(String userId) {
    return find("userId = ?1", userId).stream().toList();
  }
}
