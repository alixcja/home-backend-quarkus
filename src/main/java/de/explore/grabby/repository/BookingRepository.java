package de.explore.grabby.repository;

import de.explore.grabby.model.booking.Booking;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {
}