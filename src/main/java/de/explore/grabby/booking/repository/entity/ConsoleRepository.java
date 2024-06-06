package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Console;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsoleRepository implements PanacheRepository<Console> {
    public void updateConsole(long id, Console console) {
        Console consoleToUpdate = findById(id);
        if (consoleToUpdate != null) {
            consoleToUpdate.setName(console.getName());
            consoleToUpdate.setDescription(console.getDescription());
            persist(consoleToUpdate);
        }
    }

    public void archiveConsole(long id) {
        Console consoleToArchive = findById(id);
        if (consoleToArchive != null) {
            consoleToArchive.setIsArchived(!consoleToArchive.getIsArchived());
            persist(consoleToArchive);
        }
    }
}
