package de.explore.grabby.booking.rest.booking;

import de.explore.grabby.booking.model.Booking;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.BookingRepository;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import de.explore.grabby.booking.rest.BookingResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@TestHTTPEndpoint(BookingResource.class)
@QuarkusTest
@TestSecurity(authorizationEnabled = false)
class BookingResourceTests {

  private final BookingRepository bookingRepository;
  private final BookingEntityRepository bookingEntityRepository;
  private Booking booking1;
  private Booking booking2;
  private Game game;
  private Booking booking7;

  @Inject
  public BookingResourceTests(BookingRepository bookingRepository, BookingEntityRepository bookingEntityRepository) {
    this.bookingRepository = bookingRepository;
    this.bookingEntityRepository = bookingEntityRepository;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    game = new Game("Let's dance!", "This is a fun dance game", "Nintendo Switch");
    Console console = new Console("Nintendo Switch", "blue-yellow");

    bookingEntityRepository.persist(game, console);

    booking1 = new Booking("ghi789", game, LocalDate.now(), LocalDate.now().plusDays(2));
    booking2 = new Booking("ghi789", game, LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));
    Booking booking3 = new Booking("ghi789", game, LocalDate.now().minusDays(5), LocalDate.now().minusDays(3));
    booking3.setIsReturned(true);
    Booking booking4 = new Booking("ghi789", game, LocalDate.now().minusDays(15), LocalDate.now().minusDays(10));

    Booking booking5 = new Booking("abc123", console, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10));
    Booking booking6 = new Booking("def456", console, LocalDate.now().plusDays(15), LocalDate.now().plusDays(20));
    booking7 = new Booking("def456", console, LocalDate.now().minusDays(1), LocalDate.now().plusDays(3));

    bookingRepository.persist(booking1, booking2, booking3, booking4, booking5, booking6, booking7);
  }

  @Test
  void shouldReturnBookingById() {
    given()
            .when()
            .pathParams("id", booking1.getId())
            .get("/{id}")
            .then()
            .statusCode(SC_OK)
            .body("bookingEntity.id", is(3));
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  @OidcSecurity(
          claims = {
                  @Claim(key = "sub", value = "ghi789")
          }
  )
  void shouldReturnAllBookings() {
    given()
            .when()
            .get()
            .then()
            .statusCode(SC_OK)
            .body("size()", is(4));
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  @OidcSecurity(
          claims = {
                  @Claim(key = "sub", value = "ghi789")
          }
  )
  void shouldReturnOverdueBooking() {
    given()
            .when()
            .queryParam("status", "overdue")
            .get()
            .then()
            .statusCode(SC_OK)
            .body("size()", is(1))
            .body("[0].isReturned", is(false));
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  @OidcSecurity(
          claims = {
                  @Claim(key = "sub", value = "ghi789")
          }
  )
  void shouldReturnCurrentAndInFutureBookings() {
    given()
            .when()
            .queryParam("status", "upcoming")
            .get()
            .then()
            .statusCode(SC_OK)
            .body("size()", is(2));
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  @OidcSecurity(
          claims = {
                  @Claim(key = "sub", value = "ghi789")
          }
  )
  void shouldPersistNewBooking() {
    Booking newBooking = new Booking("user-2", game, LocalDate.now().plusDays(2), LocalDate.now().plusDays(40));

    given()
            .when()
            .contentType(MediaType.APPLICATION_JSON)
            .body(List.of(newBooking))
            .post()
            .then()
            .statusCode(SC_CREATED);
  }

  @Test
  void shouldCancelBooking() {
    given()
            .when()
            .pathParams("id", booking2.getId())
            .put("/{id}/cancel")
            .then()
            .statusCode(SC_NO_CONTENT);
  }

  @Test
  void shouldReturnBooking() {
    given()
            .when()
            .pathParams("id", booking7.getId())
            .put("/{id}/return")
            .then()
            .statusCode(SC_NO_CONTENT);
  }

  @Test
  void shouldExtendBooking() {
    given()
            .when()
            .pathParams("id", booking1.getId())
            .body(2)
            .put("/{id}/extend")
            .then()
            .statusCode(SC_NO_CONTENT);
  }

  @Test
  void shouldNotExtendBookingDueOtherBookings() {
    createAnotherBooking();
    given()
            .when()
            .pathParams("id", booking1.getId())
            .body(2)
            .put("/{id}/extend")
            .then()
            .statusCode(SC_BAD_REQUEST);
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
            .pathParams("id", booking1.getId())
            .body(9)
            .put("/{id}/extend")
            .then()
            .statusCode(SC_BAD_REQUEST);
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  void shouldGetAllBookingsByEntityAndStartAndEndDate() {
    given()
            .when()
            .queryParam("entityId", game.getId())
            .queryParam("start", LocalDate.now().minusDays(8L).format(DateTimeFormatter.ISO_LOCAL_DATE))
            .queryParam("end", LocalDate.now().plusDays(22L).format(DateTimeFormatter.ISO_LOCAL_DATE))
            .get()
            .then()
            .statusCode(SC_OK)
            .body("$", hasSize(3));
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  void shouldReturnNotFoundForBookingsByEntityAndStartAndEndDateDueInvalidEntityId() {
    given()
            .when()
            .queryParam("entityId",999L)
            .queryParam("start", LocalDate.now().minusDays(8L).format(DateTimeFormatter.ISO_LOCAL_DATE))
            .queryParam("end", LocalDate.now().plusDays(22L).format(DateTimeFormatter.ISO_LOCAL_DATE))
            .get()
            .then()
            .statusCode(SC_NOT_FOUND);
  }

  // TODO: Might need to be refactored to return 204
  @Test
  @TestSecurity(user = "Hans Müller")
  void shouldReturnEmptyListBecauseThereAreNoBookingsByEntityAndStartAndEndDate() {
    given()
            .when()
            .queryParam("entityId", game.getId())
            .queryParam("start", LocalDate.now().plusMonths(2L).format(DateTimeFormatter.ISO_LOCAL_DATE))
            .queryParam("end", LocalDate.now().plusMonths(3L).format(DateTimeFormatter.ISO_LOCAL_DATE))
            .get()
            .then()
            .log()
            .all()
            .statusCode(SC_OK)
            .body("$", hasSize(0));
  }

  @AfterEach
  @Transactional
  void tearDown() {
    bookingRepository.deleteAll();
    bookingEntityRepository.deleteAll();
  }
}
