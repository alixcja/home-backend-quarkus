package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class BookingEntityRepository implements PanacheRepository<BookingEntity> {

    public BookingEntity getEntityById(long id) {
        return findById(id);
    }

    public void archiveEntityById(long id) {
        BookingEntity entityToArchive = findById(id);
        {
            if (entityToArchive != null) {
                entityToArchive.setIsArchived(!entityToArchive.getIsArchived());
            }
        }
    }
}