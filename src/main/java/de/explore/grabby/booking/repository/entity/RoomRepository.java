package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Room;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoomRepository implements PanacheRepository<Room> {
    public void updateRoom(long id, Room room) {
        Room roomToUpdate = findById(id);
        if (roomToUpdate != null) {
            roomToUpdate.setName(room.getName());
            roomToUpdate.setDescription(room.getDescription());
            persist(roomToUpdate);
        }
    }

    public void archiveRoom(long id) {
        Room roomToArchive = findById(id);
        if (roomToArchive != null) {
            roomToArchive.setArchived(!roomToArchive.getArchived());
            persist(roomToArchive);
        }
    }
}
