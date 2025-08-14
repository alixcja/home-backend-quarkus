package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class ConsoleAccessoryRepository implements PanacheRepository<ConsoleAccessory> {
  private static final Logger LOG = LoggerFactory.getLogger(ConsoleAccessoryRepository.class);

  public long persistConsoleAccessory(ConsoleAccessory consoleAccessory) {
    persist(consoleAccessory);
    LOG.info("New entity with id {} was persisted", consoleAccessory.getId());
    return consoleAccessory.getId();
  }

  public List<ConsoleAccessory> getAllConsoleAccessories() {
    return listAll();
  }

  public void updateConsoleAccessory(long id, ConsoleAccessory consoleAccessory) {
    ConsoleAccessory consoleAccessoryToUpdate = findById(id);
    consoleAccessoryToUpdate.setName(consoleAccessory.getName());
    consoleAccessoryToUpdate.setDescription(consoleAccessory.getDescription());
    persist(consoleAccessoryToUpdate);
    LOG.info("Entity with id {} was updated", id);
  }

  public List<ConsoleAccessory> listAllNotArchived() {
    return find("isArchived is false").list();
  }

  public List<ConsoleAccessory> listAllArchived() {
    return find("isArchived is true").list();
  }
}
