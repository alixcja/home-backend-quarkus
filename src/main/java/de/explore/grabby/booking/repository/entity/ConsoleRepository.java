package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Console;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class ConsoleRepository implements PanacheRepository<Console> {
  private static final Logger LOG = LoggerFactory.getLogger(ConsoleRepository.class);

  public long persistConsole(Console console) {
    persist(console);
    LOG.info("New entity with id {} was persisted", console.getId());
    return console.getId();
  }

  public List<Console> getAllConsoles() {
    return listAll();
  }

  public void updateConsole(long id, Console console) {
    Console consoleToUpdate = findById(id);
    consoleToUpdate.setName(console.getName());
    consoleToUpdate.setDescription(console.getDescription());
    persist(consoleToUpdate);
    LOG.info("Entity with id {} was updated", console.getId());

  }
}
