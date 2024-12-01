package de.explore.grabby.booking.rest.booking;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.BookingRepository;
import de.explore.grabby.booking.repository.entity.ConsoleRepository;
import de.explore.grabby.booking.repository.entity.GameRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@TestHTTPEndpoint(BookingResource.class)
@QuarkusTest
class BookingResourceTests {

  private final BookingRepository bookingRepository;
  private final GameRepository gameRepository;
  private final ConsoleRepository consoleRepository;
  private Booking booking1;
  private Booking booking2;
  private Game game;

  @Inject
  public BookingResourceTests(BookingRepository bookingRepository, GameRepository gameRepository, ConsoleRepository consoleRepository) {
    this.bookingRepository = bookingRepository;
    this.gameRepository = gameRepository;
    this.consoleRepository = consoleRepository;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    game = new Game("Let's dance!", "This is a fun dance game", "Nintendo Switch");
    Console console = new Console("Nintendo Switch", "blue-yellow");

    gameRepository.persist(game);
    consoleRepository.persist(console);

    booking1 = new Booking("user-1", game, LocalDate.now(), LocalDate.now().plusDays(2));
    booking2 = new Booking("user-1", game, LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));

    bookingRepository.persist(booking1, booking2);
  }

  @Test
  void shouldReturnBookingById() {
    given()
            .when()
            .pathParams("id", booking1.getBookingId())
            .get("/{id}")
            .then()
            .statusCode(200)
            .body("bookedBookingEntity.id", is(booking1.getBookedBookingEntity().getId()));
  }

  @Test
  void shouldReturnAllBookings() {
    given()
            .when()
            .get()
            .then()
            .statusCode(200)
            .body("size()", is(2));
  }

  @Test
  void shouldPersistNewBooking() {
    Booking newBooking = new Booking("user-2", game, LocalDate.now().plusDays(2), LocalDate.now().plusDays(40));

    given()
            .when()
            .contentType("application/json")
            .body(List.of(newBooking))
            .post("/new")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldCancelBooking() {
    given()
            .when()
            .pathParams("id", booking2.getBookingId())
            .put("/cancel/{id}")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldReturnBooking() {
    given()
            .when()
            .pathParams("id", booking1.getBookingId())
            .put("/return/{id}")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldExtendBooking() {
    given()
            .when()
            .pathParams("id", booking1.getBookingId())
            .body(2)
            .put("/extend/{id}")
            .then()
            .statusCode(200)
            .body(is("true"));
  }

  @Test
  void shouldNotExtendBookingDueOtherBookings() {
    createAnotherBooking();
    given()
            .when()
            .pathParams("id", booking1.getBookingId())
            .body(2)
            .put("/extend/{id}")
            .then()
            .statusCode(500);
  }

  @Transactional
  public void createAnotherBooking() {
    Booking booking3 = new Booking("user-1", game, LocalDate.now().plusDays(3), LocalDate.now().plusDays(8));
    bookingRepository.persist(booking3);
  }

  @Test
  void shouldNotExtendBookingDueToMuchDays() {
    given()
            .when()
            .pathParams("id", booking1.getBookingId())
            .body(9)
            .put("/extend/{id}")
            .then()
            .statusCode(500);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    bookingRepository.deleteAll();
    gameRepository.deleteAll();
    consoleRepository.deleteAll();
  }
}
