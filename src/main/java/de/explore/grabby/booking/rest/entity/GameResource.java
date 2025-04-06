package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.GameRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Path("/games")
@Tag(name = "Game", description = "Operations related to video games")
public class GameResource {

  @Inject
  GameRepository gameRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get all games", description = "Returns a list of all games")
  @APIResponse(responseCode = "200", description = "Successfully retrieved all games",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game[].class)))
  public List<Game> getAllGames() {
    return gameRepository.getAllGames();
  }

  @RolesAllowed("${admin-role}")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  @Operation(summary = "Add a new game", description = "Adds a new game to the database")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "Game created successfully",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
          @APIResponse(responseCode = "400", description = "Invalid input")
  })
  public Response addGame(@Valid @NotNull Game game) {
    long id = gameRepository.persistGame(game);
    return Response.status(Response.Status.CREATED).entity(id).build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  @Operation(summary = "Update game", description = "Updates the details of an existing game")
  @APIResponses({
          @APIResponse(responseCode = "204", description = "Game updated successfully"),
          @APIResponse(responseCode = "404", description = "Game with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the game to be updated", required = true)
  public void updateGame(@PathParam("id") long id, @Valid @NotNull Game game) {
    ensureGameExists(id);
    gameRepository.updateGame(id, game);
  }

  public void ensureGameExists(long id) {
    Optional<Game> byId = gameRepository.findByIdOptional(id);
    if (byId.isEmpty()) {
      throw new NotFoundException("Game with id " + id + " was not found");
    }
  }
}
