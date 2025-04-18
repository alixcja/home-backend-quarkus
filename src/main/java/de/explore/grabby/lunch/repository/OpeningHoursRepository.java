package de.explore.grabby.lunch.repository;

import de.explore.grabby.lunch.model.OpeningHours;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OpeningHoursRepository implements PanacheRepository<OpeningHours> {
}
