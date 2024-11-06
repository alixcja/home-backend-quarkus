package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.repository.entity.ConsoleAccessoryRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@TestHTTPEndpoint(ConsoleAccessoryResource.class)
@QuarkusTest
class ConsoleAccessoryResourceTests {

  private ConsoleAccessory consoleAccessory2;
  private final ConsoleAccessoryRepository consoleAccessoryRepository;

  @Inject
  public ConsoleAccessoryResourceTests(ConsoleAccessoryRepository consoleAccessoryRepository) {
    this.consoleAccessoryRepository = consoleAccessoryRepository;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    ConsoleAccessory consoleAccessory1 = new ConsoleAccessory("Joycon", "blue-yellow", "Nintendo Switch");
    consoleAccessory2 = new ConsoleAccessory("Steering Wheel for Joycon", "black", "Nintendo Switch");

    consoleAccessoryRepository.persist(consoleAccessory1, consoleAccessory2);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    consoleAccessoryRepository.deleteAll();
  }

  @Test
  void shouldReturnAListWithTwoConsoleAccessorys() {
    given()
            .when()
            .get()
            .then()
            .statusCode(200)
            .body("size()", is(2));
  }

  @Test
  void shouldCreateNewConsoleAccessory() {
    ConsoleAccessory newConsoleAccessory = new ConsoleAccessory("PS5 Controller", "blue", "PS5");

    given()
            .when()
            .contentType("application/json")
            .body(newConsoleAccessory)
            .post("/create")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldFailCreatingNewConsoleAccessory() {
    given()
            .when()
            .contentType("application/json")
            .post("/create")
            .then()
            .statusCode(500);
  }

  @Test
  void shouldUpdateConsoleAccessory() {
    ConsoleAccessory updatedConsoleAccessory = new ConsoleAccessory("Steering Wheel for Joycon", "blue", "Nintendo Switch");

    given()
            .when()
            .contentType("application/json")
            .pathParams("id", consoleAccessory2.getId())
            .body(updatedConsoleAccessory)
            .put("/update/{id}")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldFailUpdatingConsoleAccessory() {
    given()
            .when()
            .contentType("application/json")
            .pathParams("id", "non-existing-game")
            .body(new ConsoleAccessory())
            .put("/update/{id}")
            .then()
            .statusCode(404);
  }
}