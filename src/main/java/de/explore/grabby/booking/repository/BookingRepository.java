package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
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

  public Boolean extendById(long id, long requestedDays) {
    Booking requestedBooking = findById(id);
    BookingEntity entity = requestedBooking.getBookedBookingEntity();
    LocalDateTime bookingEndDatePlus7 = requestedBooking.getEndDate().plusDays(7);
    if (requestedDays > 7) {
      throw new IllegalArgumentException("Man darf nicht mehr als sieben Tage verlängern.");
    }
    LocalDateTime requestedDate = requestedBooking.getEndDate().plusDays(requestedDays);
    List<Booking> bookingsWithRequestedEntity = findAllBookingsByEntity(entity, requestedDate);
    if (!bookingsWithRequestedEntity.isEmpty()) {
      // TODO - Create custom Excpetions
      throw new RuntimeException("Die gewünschte Entität ist leider verbucht.");
    } else {
      requestedBooking.setEndDate(requestedDate);
      persist(requestedBooking);
    }

    // wir holen uns alle buchungen mit der entität dessen startdatum nach dem enddatum des jetztigen buchung ist
    // danach nehmen wir alle startdaten und überprüfen, ob diese vor dem gewünschten datum sind
  }


  private List<Booking> findAllBookingsByEntity(BookingEntity entity, LocalDateTime requestedDate) {
    return find("bookingEntity_id = ?1 and startDate <= ?2", entity.getId(), requestedDate).stream().toList();
  }
}