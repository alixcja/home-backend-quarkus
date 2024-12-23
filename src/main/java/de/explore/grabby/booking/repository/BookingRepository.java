package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

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

  @Transactional
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

  @Transactional
  public Boolean extendById(long bookingId, long requestedDays) {
    Booking requestedBooking = findById(bookingId);
    BookingEntity entity = requestedBooking.getBookedBookingEntity();
    if (requestedDays > 7) {
      throw new IllegalArgumentException("Man darf nicht mehr als sieben Tage verlängern.");
    }
    LocalDate requestedDate = requestedBooking.getEndDate().plusDays(requestedDays);
    List<Booking> bookingsWithRequestedEntity = findAllBookingsByEntityAndStartDateAfterRequestedDate(requestedBooking.getBookingId(), entity, requestedDate);
    if (!bookingsWithRequestedEntity.isEmpty()) {
      // TODO - Create custom Excpetions
      throw new RuntimeException("Die gewünschte Entität ist leider verbucht.");
    } else {
      requestedBooking.setEndDate(requestedDate);
      persist(requestedBooking);
      return true;
    }
  }

  private List<Booking> findAllBookingsByEntityAndStartDateAfterRequestedDate(long id, BookingEntity entity, LocalDate requestedDate) {
    return find("bookingId != ?1 and bookedBookingEntity = ?2 and startDate <= ?3", id, entity, requestedDate)
            .list();
  }

  public List<Booking> listAllOverdueBookings() {
    return find("isReturned = False and endDate <= ?1", LocalDate.now()).stream().toList();
  }
}