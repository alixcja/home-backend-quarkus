package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;


@ApplicationScoped
public class BookingEntityRepository implements PanacheRepository<BookingEntity> {

  @Transactional
  public void archiveEntityById(long id) {
    BookingEntity entityToArchive = findById(id);
    if (entityToArchive != null) {
      entityToArchive.setIsArchived(true);
      persist(entityToArchive);
    }
  }

  @Transactional
  public void unarchiveEntityById(long id) {
    BookingEntity entityToUnarchive = findById(id);
    if (entityToUnarchive != null) {
      entityToUnarchive.setIsArchived(false);
      persist(entityToUnarchive);
    }
  }

  public List<BookingEntity> listAllArchived() {
    return find("isArchived is true").list();
  }

  public List<BookingEntity> listAllNotArchived() {
    return find("isArchived is false").list();
  }
}