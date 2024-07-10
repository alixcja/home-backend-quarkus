package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.booking.Booking;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {

    public void create(List<Booking> newBookings) {
        for (Booking booking : newBookings) {
            booking.setBookingDate(LocalDateTime.now());
            booking.setIsReturned(false);
            booking.setIsCancelled(false);
            persist(booking);
        }
    }

    public void cancelById(long id) {
        Booking bookingToCancel = findById(id);
        if (bookingToCancel != null) {
            bookingToCancel.setIsCancelled(true);
            persist(bookingToCancel);
        }
    }

    public void returnById(long id) {
        Booking bookingToReturn = findById(id);
        if (bookingToReturn != null) {
            bookingToReturn.setIsReturned(true);
            persist(bookingToReturn);
        }
    }

    public List<Booking> returnAllBookingsByUserId(String userId) {
        Stream<Booking> collectionOfBookingsByUserId = listAll().stream().filter(booking -> {
            return booking.getUserId().equals(userId);
        });
        return collectionOfBookingsByUserId.toList();
    }
}