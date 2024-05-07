package de.explore.grabby.repository.entity;

import de.explore.grabby.model.entity.ConsoleAccessory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsoleAccessoryRepository implements PanacheRepository<ConsoleAccessory> {
}
