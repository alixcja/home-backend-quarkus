package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.GameRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@TestHTTPEndpoint(GameResource.class)
@QuarkusTest
class GameResourceTests {

  private Game game1;
  private final GameRepository gameRepository;

  @Inject
  public GameResourceTests(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    game1 = new Game("Let's dance!", "This is a fun dance game", "Nintendo Switch");
    Game game2 = new Game("Mario Kart Deluxe 8", "Race against your friends and family!", "Nintendo Switch");

    gameRepository.persist(game1, game2);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    gameRepository.deleteAll();
  }

  @Test
  void shouldReturnAListWithTwoGames() {
    given()
            .when()
            .get()
            .then()
            .statusCode(200)
            .body("size()", is(2));
  }

  @Test
  void shouldCreateNewGame() {
    Game newGame = new Game("Super Smash Bros", "Beat everyone", "Nintendo Switch");

    given()
            .when()
            .contentType("application/json")
            .body(newGame)
            .post("/create")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldFailCreatingNewGame() {
    given()
            .when()
            .contentType("application/json")
            .post("/create")
            .then()
            .statusCode(500);
  }

  @Test
  void shouldUpdateGame() {
    Game updatedGame = new Game(game1.getName(), "updated description", game1.getConsoleType());

    given()
            .when()
            .contentType("application/json")
            .pathParams("id", game1.getId())
            .body(updatedGame)
            .put("/update/{id}")
            .then()
            .statusCode(204);
  }

  @Test
  void shouldFailUpdatingGame() {
    given()
            .when()
            .contentType("application/json")
            .pathParams("id", "non-existing-game")
            .body(new Game())
            .put("/update/{id}")
            .then()
            .statusCode(404);
  }
}