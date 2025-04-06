package de.explore.grabby.booking.rest;

import de.explore.grabby.booking.model.Users;
import de.explore.grabby.booking.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

// TODO for each resource class:
// - add dtos
// - add pagination?

@Path("/user")
@Tag(name = "Users", description = "Endpoints for user information")
@Produces(value = MediaType.APPLICATION_JSON)
public class UserResource {
  private final UserRepository userRepository;
  private final JsonWebToken jwt;

  @Inject
  public UserResource(UserRepository userRepository, JsonWebToken jwt) {
    this.userRepository = userRepository;
    this.jwt = jwt;
  }

  @GET
  @Path("/me")
  @Operation(summary = "Get current user", description = "Retrieves the user associated with the current JWT token.")
  @APIResponse(
          responseCode = "200",
          description = "User information retrieved",
          content = @Content(schema = @Schema(implementation = Users.class))
  )
  public Users getMyUser() {
    return userRepository.getMyUser(jwt);
  }

  @GET
  @Path("/{identifier}")
  @Operation(summary = "Get user by identifier", description = "Fetches user data using a unique identifier (e.g., username or user ID).")
  @APIResponse(
          responseCode = "200",
          description = "User found",
          content = @Content(schema = @Schema(implementation = Users.class))
  )
  @APIResponse(responseCode = "404", description = "User not found")
  public Users getUserById(@PathParam("identifier") String identifier) {
    return userRepository.getUserByIdentifier(identifier);
  }
}
