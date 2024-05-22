package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Console;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsoleRepository implements PanacheRepository<Console> {
}
