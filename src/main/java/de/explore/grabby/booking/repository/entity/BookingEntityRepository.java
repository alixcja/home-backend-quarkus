package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;


@ApplicationScoped
public class BookingEntityRepository implements PanacheRepository<BookingEntity> {

  @Transactional
  public void archiveEntityById(long id) {
    BookingEntity entityToArchive = findById(id);
    entityToArchive.setIsArchived(true);
    persist(entityToArchive);
  }

  @Transactional
  public void unarchiveEntityById(long id) {
    BookingEntity entityToUnarchive = findById(id);
    entityToUnarchive.setIsArchived(false);
    persist(entityToUnarchive);
  }

  public List<BookingEntity> listAllArchived() {
    return find("isArchived is true").list();
  }

  public List<BookingEntity> listAllNotArchived() {
    return find("isArchived is false").list();
  }

  public List<BookingEntity> newEntityWasAdded() {
    return find("addedOn >= ?1", LocalDate.now().minusDays(7)).stream().toList();
  }
}