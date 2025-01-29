package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Console;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ConsoleRepository implements PanacheRepository<Console> {
    public boolean persistConsole(Console consoleToPersist) {
        if (!Objects.isNull(consoleToPersist)) {
            persist(consoleToPersist);
            return true;
        }
        return false;
    }

    public List<Console> getAllConsoles() {
        return listAll();
    }

    public void updateConsole(long id, Console console) {
        Console consoleToUpdate = findById(id);
        if (consoleToUpdate != null) {
            consoleToUpdate.setName(console.getName());
            consoleToUpdate.setDescription(console.getDescription());
            persist(consoleToUpdate);
        }
    }
}
