package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {

  @Transactional
  public void create(List<Booking> newBookings) {
    for (Booking booking : newBookings) {
      booking.setBookingDate(LocalDate.now());
      booking.setIsReturned(false);
      booking.setIsCancelled(false);
      persist(booking);
    }
  }

  public void cancelById(long id) {
    Booking bookingToCancel = findById(id);
    if (bookingToCancel != null && bookingToCancel.getStartDate().isAfter(LocalDate.now())) {
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
    if (requestedDays > 7) {
      throw new IllegalArgumentException("Man darf nicht mehr als sieben Tage verlängern.");
    }
    LocalDate requestedDate = requestedBooking.getEndDate().plusDays(requestedDays);
    List<Booking> bookingsWithRequestedEntity = findAllBookingsByEntity(requestedBooking.getBookingId(), entity, requestedDate);
    if (!bookingsWithRequestedEntity.isEmpty()) {
      // TODO - Create custom Excpetions
      throw new RuntimeException("Die gewünschte Entität ist leider verbucht.");
    } else {
      requestedBooking.setEndDate(requestedDate);
      persist(requestedBooking);
      return true;
    }
  }

  private List<Booking> findAllBookingsByEntity(long id, BookingEntity entity, LocalDate requestedDate) {
    return find("bookingId != ?1 and bookedBookingEntity = ?2 and startDate <= ?3", id, entity, requestedDate).stream().toList();
  }
}