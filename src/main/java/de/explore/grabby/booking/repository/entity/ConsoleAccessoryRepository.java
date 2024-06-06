package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsoleAccessoryRepository implements PanacheRepository<ConsoleAccessory> {
    public void updateConsoleAccessory(long id, ConsoleAccessory consoleAccessory) {
        ConsoleAccessory consoleAccessoryToUpdate = findById(id);
        if (consoleAccessoryToUpdate != null) {
            consoleAccessoryToUpdate.setName(consoleAccessory.getName());
            consoleAccessoryToUpdate.setDescription(consoleAccessory.getDescription());
            persist(consoleAccessoryToUpdate);
        }
    }

    public void archiveConsoleAccessory(long id) {
        ConsoleAccessory consoleAccessoryToArchive = findById(id);
        if (consoleAccessoryToArchive != null) {
            consoleAccessoryToArchive.setIsArchived(!consoleAccessoryToArchive.getIsArchived());
            persist(consoleAccessoryToArchive);
        }
    }
}
