package de.explore.grabby.lunch.rest;

import de.explore.grabby.lunch.model.MenuCard;
import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.repository.MenuCardRepository;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(MenuCardResource.class)
@TestSecurity(authorizationEnabled = false)
class MenuCardResourceTest {
  @Inject
  MenuCardRepository menuCardRepository;

  @Inject
  ShopRepository shopRepository;

  private Shop amici;
  private Shop songque;
  private MenuCard menuCardSongQue;

  @BeforeEach
  @Transactional
  void setUp() {
    amici = new Shop();
    amici.setWebsite("www.amici-salate.de");
    amici.setName("Amici Salate");

    songque = new Shop();
    songque.setName("Songque");
    songque.setWebsite("www.songque.com");
    shopRepository.persist(amici, songque);

    MenuCard menuCard1 = new MenuCard();
    menuCard1.setShop(amici);
    menuCard1.setNumber(1);
    menuCard1.setFileName(UUID.randomUUID().toString());

    MenuCard menuCard2 = new MenuCard();
    menuCard2.setShop(amici);
    menuCard2.setNumber(2);
    menuCard2.setFileName(UUID.randomUUID().toString());

    menuCardSongQue = new MenuCard();
    menuCardSongQue.setShop(songque);
    menuCardSongQue.setNumber(1);
    menuCardSongQue.setFileName(UUID.randomUUID().toString());

    menuCardRepository.persist(menuCard1, menuCardSongQue, menuCard2);
  }

  @Test
  void shouldGetAllMenuCardsForShop1() {
    given()
            .pathParams("id", amici.id)
            .when()
            .get()
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize(2));
  }

  @Test
  void shouldUploadNewMenuCardForSongque() {
    String imageString = "This is a menu";
    InputStream imageStream = new ByteArrayInputStream(imageString.getBytes());

    given()
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .multiPart("file", "songque2.png", imageStream)
            .multiPart("number", "2")
            .pathParams("id", songque.id)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);

    assertEquals(2, menuCardRepository.findAllMenuCardsByShop(songque.id).size());
  }

  @Test
  void shouldReplaceMenuCard1ForSongque() {
    String imageString = "This is a menu";
    InputStream imageStream = new ByteArrayInputStream(imageString.getBytes());

    given()
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .multiPart("file", "songque1.png", imageStream)
            .multiPart("number", "1")
            .pathParams("id", songque.id)
            .pathParams("menuCardId", menuCardSongQue.id)
            .when()
            .put("/{menuCardId}")
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);

    assertEquals(1, menuCardRepository.findAllMenuCardsByShop(songque.id).size());
  }

  @AfterEach
  @Transactional
  void tearDown() {
    menuCardRepository.deleteAll();
    shopRepository.deleteAll();
  }
}