package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.repository.entity.ConsoleRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@TestHTTPEndpoint(ConsoleResource.class)
@QuarkusTest
class ConsoleResourceTests {

  private Console console2;
  private final ConsoleRepository consoleRepository;

  @Inject
  public ConsoleResourceTests(ConsoleRepository consoleRepository) {
    this.consoleRepository = consoleRepository;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    Console console1 = new Console("Nintendo Switch", "blue-yellow");
    console2 = new Console("Nintendo Switch", "red-blue");

    consoleRepository.persist(console1, console2);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    consoleRepository.deleteAll();
  }

  @Test
  void shouldReturnAListWithTwoConsoles() {
    given()
            .when()
            .get()
            .then()
            .statusCode(200)
            .body("size()", is(2));
  }

  @Test
  void shouldCreateNewConsole() {
    Console newConsole = new Console("Steam deck", "This is a steam deck");

    given()
            .when()
            .contentType("application/json")
            .body(newConsole)
            .post("/create")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldFailCreatingNewConsole() {
    given()
            .when()
            .contentType("application/json")
            .post("/create")
            .then()
            .statusCode(500);
  }

  @Test
  void shouldUpdateConsole() {
    Console updatedConsole = new Console("Nintendo Switch", "updated description");

    given()
            .when()
            .contentType("application/json")
            .pathParams("id", console2.getId())
            .body(updatedConsole)
            .put("/update/{id}")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldFailUpdatingConsole() {
    given()
            .when()
            .contentType("application/json")
            .pathParams("id", "non-existing-game")
            .body(new Console())
            .put("/update/{id}")
            .then()
            .statusCode(404);
  }
}