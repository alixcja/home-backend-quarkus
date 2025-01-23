package de.explore.grabby.booking.service;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.repository.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BookingService {
  public static final int MAX_EXTENDING_DAY = 7;
  @Inject
  BookingRepository repository;

  public Boolean extendById(long bookingId, long requestedDays) {
    Booking requestedBooking = repository.findById(bookingId);
    BookingEntity entity = requestedBooking.getBookedBookingEntity();
    validateRequestedDays(requestedDays);
    LocalDate requestedEndDate = requestedBooking.getEndDate().plusDays(requestedDays);
    ensureEntityIsAvailable(requestedBooking.getBookingId(), entity, requestedBooking.getEndDate(), requestedEndDate);
    return repository.extendBooking(requestedBooking, requestedEndDate);
  }

  private void ensureEntityIsAvailable(long bookingId, BookingEntity entity, LocalDate endDate, LocalDate requestedEndDate) {
    List<Booking> bookingsWithRequestedEntity = repository.findAllBookingsByEntityAndByStartDateAfterRequestedDate(bookingId, entity, requestedEndDate, endDate);

    if (!bookingsWithRequestedEntity.isEmpty()) {
      // TODO - Create custom Excpetions
      throw new RuntimeException("Die gewünschte Entität ist leider verbucht.");
    }
  }

  private void validateRequestedDays(long requestedDays) {
    if (requestedDays > MAX_EXTENDING_DAY) {
      throw new IllegalArgumentException("Man darf nicht mehr als sieben Tage verlängern.");
    }
  }
}
