package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.Booking;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BookingRepositoryTest {

  @Inject
  BookingRepository bookingRepository;

  @Inject
  BookingEntityRepository bookingEntityRepository;
  private Game game;
  private Console console;
  private Booking booking1;

  @BeforeEach
  @Transactional
  void setUp() {
    game = new Game("Let's dance!", "This is a fun dance game", "Nintendo Switch");
    console = new Console("Nintendo Switch", "blue-yellow");
    bookingEntityRepository.persist(game, console);

    booking1 = new Booking("user-1", game, LocalDate.now(), LocalDate.now().plusDays(3L));
    Booking booking2 = new Booking("user-2", game, LocalDate.now().plusMonths(1L), LocalDate.now().plusMonths(1L).plusDays(6L));
    Booking booking3 = new Booking("user-1", console, LocalDate.now(), LocalDate.now().plusDays(5L));
    Booking booking4 = new Booking("user-1", game, LocalDate.now(), LocalDate.now().plusDays(3L));
    booking4.setIsCancelled(true);
    bookingRepository.persist(booking1, booking2, booking3, booking4);
    bookingRepository.persist(booking1, booking2, booking3, booking4);
  }

  @Test
  @TestTransaction
  void shouldReturnOnlyBookingByEntityAndStartAndEndDate() {
    // when
    List<Booking> bookings = bookingRepository.listAllByEntityAndStartAndEnd(game.getId(), LocalDate.now().minusDays(3L), LocalDate.now().plusMonths(1L));

    // then
    assertEquals(1L, bookings.size());
    assertEquals(booking1.getId(), bookings.getFirst().getId());
    assertEquals(game.getId(), bookings.getFirst().getBookingEntity().getId());
  }

  @Test
  @TestTransaction
  void shouldReturnEmptyBecauseThereAreNoBookingsByEntityAndStartAndEndDate() {
    // when
    List<Booking> bookings = bookingRepository.listAllByEntityAndStartAndEnd(game.getId(), LocalDate.now().plusMonths(3L), LocalDate.now().plusMonths(4L));

    // then
    assertEquals(Collections.emptyList(), bookings);
  }
}