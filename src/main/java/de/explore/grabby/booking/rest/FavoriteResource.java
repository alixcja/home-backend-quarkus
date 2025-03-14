package de.explore.grabby.booking.rest;

import de.explore.grabby.booking.model.Favorite;
import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.repository.FavoriteRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/favorites")
public class FavoriteResource {
  private final FavoriteRepository favoriteRepository;
  private final JsonWebToken jwt;

  @Inject
  public FavoriteResource(FavoriteRepository favoriteRepository, JsonWebToken jwt) {
    this.favoriteRepository = favoriteRepository;
    this.jwt = jwt;
  }

  @GET
  public List<Favorite> getAllFavorites() {
    return favoriteRepository.listAllFavorites(jwt.getSubject());
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addNewFavorite(BookingEntity entity) {
    Favorite favorite = favoriteRepository.addNewFavorite(entity, jwt.getSubject());
    return Response.status(Response.Status.CREATED).entity(favorite).build();
  }

  @DELETE
  @Path("/{id}")
  public void deleteFavorite(@PathParam("id") long id) {
    favoriteRepository.delete(id);
  }
}
