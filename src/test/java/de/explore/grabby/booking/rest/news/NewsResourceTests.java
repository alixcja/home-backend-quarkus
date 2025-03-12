package de.explore.grabby.booking.rest.news;

import de.explore.grabby.booking.model.Booking;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.BookingRepository;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import de.explore.grabby.booking.rest.NewsResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestHTTPEndpoint(NewsResource.class)
public class NewsResourceTests {
  @Inject
  BookingEntityRepository bookingEntityRepository;

  @Inject
  BookingRepository bookingRepository;

  @Test
  @TestSecurity(user = "Peter Lustig")
  @OidcSecurity(
          claims = {
                  @Claim(key = "sub", value = "peters-identifier")
          }
  )
  void shouldReturnAllFourNewsWithCorrectEntity() {
    setUpAllNews();
    given()
            .when()
            .get()
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize(4));
  }

  @Test
  @TestSecurity(user = "Peter Lustig")
  @OidcSecurity(
          claims = {
                  @Claim(key = "sub", value = "peters-identifier")
          }
  )
  void shouldReturnAllTwoNewEntitiesNews() {
    setUpTwoNewEntities();
    given()
            .when()
            .get()
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize(2));
  }

  @Transactional
  public void setUpAllNews() {
    Game game = new Game("Red dead redemption", "Best western game", "XBOX 360");
    bookingEntityRepository.persist(game);

    Booking bookingWithStartDateToday = new Booking("peters-identifier", game, LocalDate.now(), LocalDate.now().plusDays(5));
    Booking soonOverdueBooking = new Booking("peters-identifier", game, LocalDate.now().minusDays(7), LocalDate.now().plusDays(1));
    Booking overdueBooking = new Booking("peters-identifier", game, LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
    bookingRepository.persist(bookingWithStartDateToday, soonOverdueBooking, overdueBooking);
  }

  @Transactional
  public void setUpTwoNewEntities() {
    Game game = new Game("Red dead redemption", "Best western game", "XBOX 360");
    Console console = new Console("Xbox 360", "It's an old console");
    Booking soonOverdueBooking = new Booking("peters-identifier", game, LocalDate.now().minusDays(4), LocalDate.now().plusDays(3));

    bookingEntityRepository.persist(game, console);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    bookingRepository.deleteAll();
    bookingEntityRepository.deleteAll();
  }
}
