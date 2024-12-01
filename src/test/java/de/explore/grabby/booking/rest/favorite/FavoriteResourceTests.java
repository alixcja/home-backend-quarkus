package de.explore.grabby.booking.rest.favorite;

import de.explore.grabby.booking.model.Favorite;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.FavoriteRepository;
import de.explore.grabby.booking.repository.entity.GameRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestHTTPEndpoint(FavoriteResource.class)
class FavoriteResourceTests {

  private final FavoriteRepository favoriteRepository;
  private final GameRepository gameRepository;
  private Game game3;
  private Favorite favorite1;

  @Inject
  public FavoriteResourceTests(FavoriteRepository favoriteRepository, GameRepository gameRepository) {
    this.favoriteRepository = favoriteRepository;
    this.gameRepository = gameRepository;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    Game game1 = new Game("Let's dance!", "This is a fun dance game", "Nintendo Switch");
    Game game2 = new Game("Mario Kart Deluxe 8", "Race against your friends and family!", "Nintendo Switch");
    game3 = new Game("Super Smash Bros Ultimate", "Legendäre Kämpfer und berühmte Spielwelten treffen im ultimativen Showdown aufeinander. Dies ist der neueste Titel in der Super Smash Bros.-Reihe für Nintendo Switch", "Nintendo Switch");

    gameRepository.persist(game1, game2, game3);

    favorite1 = new Favorite("user-1", game1);
    Favorite favorite2 = new Favorite("user-1", game2);

    favoriteRepository.persist(favorite1, favorite2);
  }

  @Test
  void getAllFavorites() {
    given()
            .when()
            .get()
            .then()
            .statusCode(200)
            .body("size()", is(2));
  }

  @Test
  void shouldPersistNewFavorite() {
    Favorite favorite = new Favorite("user-1", game3);

    given()
            .when()
            .contentType(MediaType.APPLICATION_JSON)
            .body(favorite)
            .post()
            .then()
            .statusCode(204);
  }

  @Test
  void shouldDeleteFavoriteById() {
    given()
            .when()
            .pathParams("id", favorite1.getFavoriteId())
            .delete("/{id}")
            .then()
            .statusCode(204);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    favoriteRepository.deleteAll();
    gameRepository.deleteAll();
  }
}