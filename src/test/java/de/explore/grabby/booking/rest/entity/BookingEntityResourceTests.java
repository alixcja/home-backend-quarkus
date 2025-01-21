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

    repository.persist(game1, game2);
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

    BookingEntity newGame = repository.findById(Long.valueOf(game1.getId()));
    assertNotNull(newGame.getImage());
  }

  // validate if image is there
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
    service.uploadImageForEntity((long) game1.getId(), uploadForm);
    assertNotNull(repository.findById(game1.getId()).getImage());

    given().when().
            pathParams("id", game1.getId())
            .get("/{id}/image")
            .then()
            .statusCode(SC_OK)
            .body(notNullValue());
  }

  @Test
  void shouldReturnNoImageOfGame() {
    given().when().
            pathParams("id", game2.getId())
            .get("/{id}/image")
            .then()
            .statusCode(SC_NO_CONTENT);
  }


  @AfterEach
  @Transactional
  void tearDown() {
    // TODO: Start minio as dev service or delete all images after each test
    repository.deleteAll();
  }
}
