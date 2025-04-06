package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.Favorite;
import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class FavoriteRepository implements PanacheRepository<Favorite> {

  private static final Logger LOG = LoggerFactory.getLogger(FavoriteRepository.class);

  @Transactional
  public Favorite addNewFavorite(BookingEntity entity, String userId) {
    Favorite newFav = new Favorite();
    newFav.setUserId(userId);
    newFav.setFavorite(entity);
    persist(newFav);
    LOG.info("New favorite with id {} was persisted", newFav.getId());
    return newFav;
  }

  @Transactional
  public void delete(long id) {
    deleteById(id);
    LOG.info("Favorite with id {} was deleted", id);
  }

  public List<Favorite> listAllFavorites(String userId) {
    return find("userId = ?1", userId).stream().toList();
  }
}
