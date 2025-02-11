package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.Booking;
import de.explore.grabby.booking.model.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {

  @Transactional
  public void create(List<Booking> newBookings, String userId) {
    for (Booking booking : newBookings) {
      booking.setBookingDate(LocalDate.now());
      booking.setIsReturned(false);
      booking.setIsCancelled(false);
      booking.setUserId(userId);
      persist(booking);
    }
  }

  @Transactional
  public void cancelById(long id) {
    Booking bookingToCancel = findById(id);
    bookingToCancel.setIsCancelled(true);
    persist(bookingToCancel);
  }

  public void returnById(long id) {
    Booking bookingToReturn = findById(id);
    bookingToReturn.setIsReturned(true);
    persist(bookingToReturn);
  }

  @Transactional
  public void extendBooking(long id, LocalDate requestedDate) {
    Booking requestedBooking = findById(id);
    requestedBooking.setEndDate(requestedDate);
    persist(requestedBooking);
  }

  public List<Booking> findAllBookingsByEntityAndByStartDateAfterRequestedDate(long id, BookingEntity entity, LocalDate requestedDate, LocalDate endDate) {
    return find("id != ?1 and bookingEntity = ?2 and startDate <= ?3 and endDate >= ?4", id, entity, requestedDate, endDate)
            .list();
  }

  public List<Booking> listAllOverdueBookings(String userId) {
    return find("isReturned = False and endDate <= ?1 and userId = ?2", LocalDate.now(), userId).stream().toList();
  }

  public List<Booking> listAllCurrentAndInFutureBookings(String userId) {
    return find("startDate >= ?1 and userId = ?2", LocalDate.now(), userId).stream().toList();
  }

  public List<Booking> listAllBookings(String userId) {
    return find("userId = ?1", userId).stream().toList();
  }
}