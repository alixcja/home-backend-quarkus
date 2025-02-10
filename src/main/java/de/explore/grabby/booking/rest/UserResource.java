package de.explore.grabby.booking.rest;

import de.explore.grabby.booking.model.Users;
import de.explore.grabby.booking.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/user")
public class UserResource {
  private final UserRepository userRepository;

  @Inject
  public UserResource(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GET
  @Path("/{identifier}")
  public Users getUserById(@PathParam("identifier") String identifier) {
    return userRepository.getUserByIdentifier(identifier);
  }
}
