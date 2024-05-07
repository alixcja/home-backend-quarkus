package de.explore.grabby.repository.entity;

import de.explore.grabby.model.entity.Room;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoomRepository implements PanacheRepository<Room> {
}
