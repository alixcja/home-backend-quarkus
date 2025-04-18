package de.explore.grabby.lunch.rest;

import de.explore.grabby.lunch.model.Address;
import de.explore.grabby.lunch.model.OpeningHours;
import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.model.Weekday;
import de.explore.grabby.lunch.repository.ShopRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static de.explore.grabby.lunch.model.Weekday.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(ShopResource.class)
@TestSecurity(authorizationEnabled = false)
public class ShopResourceTests {

  @Inject
  ShopRepository shopRepository;
  private Shop shop1;
  private Shop shop2;

  @BeforeEach
  @Transactional
  void setUp() {
    Address addressSongQue = new Address();
    addressSongQue.setPostalCode(85276);
    addressSongQue.setCity("Pfaffenhofen an der Ilm");
    addressSongQue.setStreetName("Schweitenkirchener Straße");
    addressSongQue.setStreetNumber("3");

    shop1 = new Shop();
    shop1.setName("Song Que");
    shop1.setPhoneNumber("08441 7899804");
    shop1.setWebsite("songque.de");
    shop1.setAddress(addressSongQue);
    /* shop1.getOpeningHours().addAll(buildOpeningHoursForSongQue(shop1));*/
    buildOpeningHoursForSongQue(shop1);

    Address addressGreenGarden = new Address();
    addressGreenGarden.setPostalCode(85276);
    addressGreenGarden.setCity("Pfaffenhofen an der Ilm");
    addressGreenGarden.setStreetName("Hauptstraße");
    addressGreenGarden.setStreetNumber("15");

    shop2 = new Shop();
    shop2.setName("Green Garden");
    shop2.setPhoneNumber("08441 1234567");
    shop2.setWebsite("greengarden.de");
    shop2.setAddress(addressGreenGarden);
    shop2.setArchived(true);
/*
    shop2.getOpeningHours().addAll(buildOpeningHoursForGreenGarden(shop2));
*/
    buildOpeningHoursForGreenGarden(shop2);


    shopRepository.persist(shop1, shop2);
  }

  private List<OpeningHours> buildOpeningHoursForGreenGarden(Shop shop) {
    List<OpeningHours> openingHoursList = new ArrayList<>();

    openingHoursList.add(new OpeningHours(MONDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(MONDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(TUESDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(TUESDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(WEDNESDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(WEDNESDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(THURSDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(THURSDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(FRIDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(FRIDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(SATURDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(SUNDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(SUNDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    return openingHoursList;
  }


  @Test
  void shouldGetShopById() {
    given()
            .pathParams("id", shop1.id)
            .when()
            .get("/{id}")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", is( shop1.id.intValue()))
            .body("name", is(shop1.getName()));
  }

  @Test
  void shouldNotGetShopByIdBecauseOfInvalidId() {
    given()
            .pathParams("id", 999L)
            .when()
            .get("/{id}")
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  void shouldGetTwoShopsAsList() {
    given()
            .when()
            .get()
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize(2));
  }

  @Test
  void shouldCreateNewShop() {
    Address address = new Address();
    address.setStreetNumber("11");
    address.setStreetName("Test-Street");
    address.setPostalCode(85276);
    address.setCity("Pfaffenhofen an der Ilm");

    Shop shop = new Shop();
    shop.setName("Kaufland");
    shop.setAddress(address);
    shop.setWebsite("https://kaufland.de");
    buildOpeningHoursForKaufland(shop);

    given()
            .body(shop)
            .contentType(MediaType.APPLICATION_JSON)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("id", notNullValue())
            .body("name", is("Kaufland"))
            .body(notNullValue())
/*
            .body("tags", hasSize(2))
*/
            .body("website", is("https://kaufland.de"))
            .body("archived", is(false));
  }

  @Test
  void shouldUpdateNewShop() {
    Address address = new Address();
    address.setStreetNumber("50");
    address.setStreetName("New-Street");
    address.setPostalCode(85276);
    address.setCity("Pfaffenhofen an der Ilm");

    shop1.setAddress(address);
    shop1.setArchived(true);

    given()
            .pathParams("id", shop1.id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(shop1)
            .when()
            .put("/{id}")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", is(shop1.id.intValue()))
            .body("name", is(shop1.getName()))
            .body("address.streetNumber", is("50"))
            .body("address.streetName", is("New-Street"))
            .body("address.postalCode", is(85276))
            .body("address.city", is("Pfaffenhofen an der Ilm"))
            .body("archived", is(true));
  }

  private List<OpeningHours> buildOpeningHoursForSongQue(Shop shop) {
    List<OpeningHours> openingHoursList = new ArrayList<>();

    openingHoursList.add(new OpeningHours(MONDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(MONDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(TUESDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(TUESDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(WEDNESDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(WEDNESDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(THURSDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(THURSDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(FRIDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(FRIDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(SATURDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    openingHoursList.add(new OpeningHours(SUNDAY, shop, LocalTime.of(11, 30), LocalTime.of(14, 30)));
    openingHoursList.add(new OpeningHours(SUNDAY, shop, LocalTime.of(17, 0), LocalTime.of(22, 0)));

    return openingHoursList;
  }

  public List<OpeningHours> buildOpeningHoursForKaufland(Shop shop) {
    List<OpeningHours> openingHoursList = new ArrayList<>();

    openingHoursList.add(new OpeningHours(Weekday.MONDAY, shop, LocalTime.of(07, 30), LocalTime.of(20, 00)));
    openingHoursList.add(new OpeningHours(Weekday.TUESDAY, shop, LocalTime.of(07, 30), LocalTime.of(20, 00)));
    openingHoursList.add(new OpeningHours(Weekday.WEDNESDAY, shop, LocalTime.of(07, 30), LocalTime.of(20, 00)));
    openingHoursList.add(new OpeningHours(Weekday.THURSDAY, shop, LocalTime.of(07, 30), LocalTime.of(20, 00)));
    openingHoursList.add(new OpeningHours(Weekday.FRIDAY, shop, LocalTime.of(07, 30), LocalTime.of(20, 00)));
    openingHoursList.add(new OpeningHours(Weekday.SATURDAY, shop, LocalTime.of(07, 30), LocalTime.of(20, 00)));

    return openingHoursList;
  }

  @AfterEach
  @Transactional
  void tearDown() {
    shopRepository.deleteAll();
  }
}
