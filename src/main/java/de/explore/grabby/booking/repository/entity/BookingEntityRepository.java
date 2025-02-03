package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class BookingEntityRepository implements PanacheRepository<BookingEntity> {

    public void archiveEntityById(long id) {
        BookingEntity entityToArchive = findById(id);
        {
            if (entityToArchive != null) {
                entityToArchive.setIsArchived(!entityToArchive.getIsArchived());
            }
        }
    }

  public List<BookingEntity> listAllArchived() {
      return find("isArchived is true").list();
  }

  public List<BookingEntity> listAllNotArchived() {
    return find("isArchived is false").list();
  }
}