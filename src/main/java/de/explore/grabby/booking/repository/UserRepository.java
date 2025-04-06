package de.explore.grabby.booking.repository;

import de.explore.grabby.booking.model.Users;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {
  private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

  public Users getUserByIdentifier(String identifier) {
    return getUserByIdentifierIfExists(identifier).orElseThrow(NotFoundException::new);
  }

  @Transactional
  public Users createUser(JsonWebToken jwt) {
    Users users = new Users(jwt.getSubject(), jwt.getClaim(Claims.family_name), jwt.getClaim(Claims.given_name));
    persist(users);
    LOG.info("User with subject {} was created", jwt.getSubject());
    return users;
  }

  private Optional<Users> getUserByIdentifierIfExists(String identifier) {
    return find("id", identifier).singleResultOptional();
  }

  public Users getMyUser(JsonWebToken jwt) {
    return getUserByIdentifierIfExists(jwt.getSubject()).orElseGet(() -> createUser(jwt));
  }
}