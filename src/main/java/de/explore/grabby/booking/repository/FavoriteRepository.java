package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.Favorite;
import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class FavoriteRepository implements PanacheRepository<Favorite> {

  @Transactional
  public void addNewFavorite(BookingEntity entity, String userId) {
    Favorite newFav = new Favorite();
    newFav.setUserId(userId);
    newFav.setFavorite(entity);
    persist(newFav);
  }

  @Transactional
  public void delete(long favoriteId) {
    deleteById(favoriteId);
  }

  public List<Favorite> listAllFavorites(String userId) {
    return find("userId = ?1", userId).stream().toList();
  }
}
