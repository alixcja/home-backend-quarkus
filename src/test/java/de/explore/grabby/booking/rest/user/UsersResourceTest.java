package de.explore.grabby.booking.rest.user;

import de.explore.grabby.booking.model.Users;
import de.explore.grabby.booking.repository.UserRepository;
import de.explore.grabby.booking.rest.UserResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
class UsersResourceTest {

  private final UserRepository userRepository;
  private Users users2;
  private Users users1;

  @Inject
  public UsersResourceTest(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    users1 = new Users();
    users1.setIdentifier("abc123");
    users1.setFirstName("Peter");
    users1.setLastName("Lustig");
    users2 = new Users();
    users2.setIdentifier("def456");
    users2.setFirstName("Hans");
    users2.setLastName("Müller");

    userRepository.persist(users1, users2);
    userRepository.getEntityManager().flush();
  }

  @Test
  @TestSecurity(user = "Peter Lustig")
          @OidcSecurity(
                  claims = {
                          @Claim(key = "family_name", value = "Lustig"),
                          @Claim(key = "given_name", value = "Peter"),
                          @Claim(key = "sub", value = "abc123")
                  }
          )
  void shouldReturnMyUser() {
    given()
            .when()
            .get("/me")
            .then()
            .statusCode(200)
            .body("identifier", is(users1.getIdentifier()));
  }

  @Test
  @TestSecurity(user = "Max Mustermann")
  @OidcSecurity(
          claims = {
                  @Claim(key = "family_name", value = "Mustermann"),
                  @Claim(key = "given_name", value = "Max"),
                  @Claim(key = "sub", value = "ghi789")
          }
  )
  void shouldCreateAndReturnMyUser() {
    given()
            .when()
            .get("/me")
            .then()
            .statusCode(200)
            .body("identifier", is("ghi789"));
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  void shouldReturnAUserById() {
    given()
            .when()
            .pathParams("identifier", users2.getIdentifier())
            .get("/{identifier}")
            .then()
            .statusCode(200)
            .body("identifier", is(users2.getIdentifier()))
            .body("firstName", is(users2.getFirstName()))
            .body("lastName", is(users2.getLastName()));
  }

  @Test
  @TestSecurity(user = "Hans Müller")
  void shouldReturn404() {
    given()
            .when()
            .pathParams("identifier", "non-existing-identifier")
            .get("/{identifier}")
            .then()
            .statusCode(404);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    userRepository.deleteAll();
  }
}