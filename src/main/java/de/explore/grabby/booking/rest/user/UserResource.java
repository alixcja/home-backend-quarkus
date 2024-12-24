package de.explore.grabby.booking.rest.user;

import de.explore.grabby.booking.model.Users;
import de.explore.grabby.booking.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/user")
public class UserResource {
  private UserRepository userRepository;
  private JsonWebToken jwt;

  @Inject
  public UserResource(UserRepository userRepository, JsonWebToken jwt) {
    this.userRepository = userRepository;
    this.jwt = jwt;
  }

  @GET
  @Path("/me")
  public Users getMyUser() {
    return userRepository.getMyUser(jwt);
  }

  @GET
  @Path("/{id}")
  public Users getUserById(@PathParam("id") String id) {
    return userRepository.getUserById(id);
  }
}
