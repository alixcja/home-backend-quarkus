package de.explore.grabby.booking.rest;

import de.explore.grabby.booking.model.Favorite;
import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.repository.FavoriteRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/favorites")
@Tag(name = "Favorites", description = "Manage user's favorite entities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FavoriteResource {
  private final FavoriteRepository favoriteRepository;
  private final JsonWebToken jwt;

  @Inject
  public FavoriteResource(FavoriteRepository favoriteRepository, JsonWebToken jwt) {
    this.favoriteRepository = favoriteRepository;
    this.jwt = jwt;
  }

  @GET
  @Operation(summary = "Get all favorites", description = "Returns all favorite entities for the authenticated user.")
  @APIResponse(
          responseCode = "200",
          description = "List of favorites retrieved",
          content = @Content(schema = @Schema(implementation = Favorite[].class))
  )
  public List<Favorite> getAllFavorites() {
    return favoriteRepository.listAllFavorites(jwt.getSubject());
  }

  @POST
  @Operation(summary = "Add new favorite", description = "Marks a booking entity as favorite for the user.")
  @APIResponses({
          @APIResponse(
                  responseCode = "201",
                  description = "Favorite added successfully",
                  content = @Content(schema = @Schema(implementation = Favorite.class))
          ),
          @APIResponse(responseCode = "400", description = "Invalid input or already favorited")
  })
  public Response addNewFavorite(BookingEntity entity) {
    Favorite favorite = favoriteRepository.addNewFavorite(entity, jwt.getSubject());
    return Response.status(Response.Status.CREATED).entity(favorite).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete favorite", description = "Removes a favorite by ID.")
  @APIResponses({
          @APIResponse(responseCode = "204", description = "Favorite deleted"),
          @APIResponse(responseCode = "404", description = "Favorite not found")
  })
  public void deleteFavorite(@PathParam("id") long id) {
    favoriteRepository.delete(id);
  }
}
