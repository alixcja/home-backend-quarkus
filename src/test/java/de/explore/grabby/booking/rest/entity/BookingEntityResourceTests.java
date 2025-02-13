package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import de.explore.grabby.booking.rest.request.UploadForm;
import de.explore.grabby.booking.service.BookingEntityService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestHTTPEndpoint(BookingEntityResource.class)
@QuarkusTest
@TestSecurity(authorizationEnabled = false)
public class BookingEntityResourceTests {
  private Game game1;
  private Game game2;

  @Inject
  BookingEntityRepository repository;

  @Inject
  BookingEntityService service;

  @Inject
  S3Client s3;

  @BeforeEach
  @Transactional
  void setUp() {
    game1 = new Game("Let's dance!", "This is a fun dance game", "Nintendo Switch");
    game2 = new Game("Mario Kart 8 Deluxe", "You will hate each other", "Nintendo Switch");
    Game game3 = new Game("Monopoly", "When playing this game, you will hate your friends and family", "Nintendo Switch");
    game3.setIsArchived(true);

    repository.persist(game1, game2, game3);
  }

  @Test
  void shouldGetEntityById() {
    given()
            .when()
            .pathParams("id", game1.getId())
            .get("/{id}")
            .then()
            .statusCode(SC_OK)
            .body("name", is(game1.getName()))
            .body("description", is(game1.getDescription()))
            .body("type", is(game1.getType()))
            .body("consoleType", is(game1.getConsoleType()))
            .body("isArchived", is(false))
            .body("addedOn", notNullValue())
            .body("image", nullValue());
  }

  @Test
  void shouldNotGetEntityById() {
    given()
            .when()
            .pathParams("id", "non-existing-id")
            .get("/{id}")
            .then()
            .statusCode(SC_NOT_FOUND);
  }

  @Test
  void shouldGetAllEntities() {
    given()
            .when()
            .get()
            .then()
            .statusCode(SC_OK)
            .body("size()", is(3));
  }

  @Test
  void shouldGetAllNotArchivedEntities() {
    given()
            .when()
            .get("/not-archived")
            .then()
            .statusCode(SC_OK)
            .body("size()", is(2));
  }

  @Test
  void shouldGetAllArchivedEntities() {
    given()
            .when()
            .get("/archived")
            .then()
            .statusCode(SC_OK)
            .body("size()", is(1));
  }

  @Test
  void shouldArchiveEntityById() {
    given()
            .when()
            .pathParams("id", game1.getId())
            .put("/{id}/archive")
            .then()
            .statusCode(SC_NO_CONTENT);
  }

  @Test
  void shouldNotArchiveEntityById() {
    given()
            .when()
            .pathParams("id", "no-existing-id")
            .put("/{id}/archive")
            .then()
            .statusCode(SC_NOT_FOUND);
  }

  @Test
  void shouldUploadImageForGame() {
    assertNull(game1.getImage());
    String imageString = "This is Mario Kart";
    InputStream imageStream = new ByteArrayInputStream(imageString.getBytes());

    given()
            .when()
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .multiPart("file", "mariokart.png", imageStream)
            .pathParams("id", game1.getId())
            .put("/{id}/image")
            .then()
            .statusCode(SC_NO_CONTENT);

    BookingEntity newGame = repository.findById(game1.getId());
    assertNotNull(newGame.getImage());
  }

  @Test
  void shouldNotUploadImageForGame() {
    given()
            .when()
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .pathParams("id", game1.getId())
            .put("/{id}/image")
            .then()
            .statusCode(SC_BAD_REQUEST);
  }

  @Test
  void shouldReturnImageOfGame() throws IOException {
    File image = File.createTempFile("tmp", ".png");
    UploadForm uploadForm = new UploadForm();
    uploadForm.file = image;
    service.uploadImageForEntity(game1.getId(), uploadForm);
    assertNotNull(repository.findById(game1.getId()).getImage());

    given()
            .when()
            .pathParams("id", game1.getId())
            .get("/{id}/image")
            .then()
            .statusCode(SC_OK)
            .body(notNullValue());
  }

  @AfterEach
  @Transactional
  void tearDown() {
    // TODO: Start minio as dev service or delete all images after each test
    repository.deleteAll();
  }
}
