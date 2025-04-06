package de.explore.grabby.booking.service;

import de.explore.grabby.booking.model.Booking;
import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.repository.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BookingService {

  @Inject
  BookingRepository repository;

  public boolean extendById(long bookingId, long requestedDays) {
    Booking requestedBooking = repository.findById(bookingId);
    LocalDate requestedEndDate = requestedBooking.getEndDate().plusDays(requestedDays);
    boolean isEntityAvailable = ensureEntityIsAvailable(requestedBooking.getId(), requestedBooking.getBookingEntity(), requestedBooking.getEndDate(), requestedEndDate);
    if (!isEntityAvailable) {
      return false;
    }
    repository.extendBooking(bookingId, requestedEndDate);
    return true;
  }

  private boolean ensureEntityIsAvailable(long bookingId, BookingEntity entity, LocalDate endDate, LocalDate requestedEndDate) {
    List<Booking> bookingsWithRequestedEntity = repository.findAllBookingsByEntityAndByStartDateAfterRequestedDate(bookingId, entity, requestedEndDate, endDate);

    return bookingsWithRequestedEntity.isEmpty();
  }
}
