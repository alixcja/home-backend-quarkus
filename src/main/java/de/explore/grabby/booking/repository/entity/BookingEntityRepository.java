package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;


@ApplicationScoped
public class BookingEntityRepository implements PanacheRepository<BookingEntity> {

  private static final Logger LOG = LoggerFactory.getLogger(BookingEntityRepository.class);

  @Transactional
  public void archiveEntityById(long id) {
    BookingEntity entityToArchive = findById(id);
    entityToArchive.setIsArchived(true);
    persist(entityToArchive);
    LOG.info("Entity with id {} was archived", id);
  }

  @Transactional
  public void unarchiveEntityById(long id) {
    BookingEntity entityToUnarchive = findById(id);
    entityToUnarchive.setIsArchived(false);
    persist(entityToUnarchive);
    LOG.info("Entity with id {} was unarchived", id);
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