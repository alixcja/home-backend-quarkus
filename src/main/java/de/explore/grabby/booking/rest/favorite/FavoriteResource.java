package de.explore.grabby.booking.rest.favorite;

import de.explore.grabby.booking.model.Favorite;
import de.explore.grabby.booking.repository.FavoriteRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/favorites")
public class FavoriteResource {
  private final FavoriteRepository favoriteRepository;

  @Inject
  public FavoriteResource(FavoriteRepository favoriteRepository) {
    this.favoriteRepository = favoriteRepository;
  }

  // TODO - Implement user logic
  @GET
  public List<Favorite> getAllFavorites() {
    return favoriteRepository.listAll();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void addNewFavorite(Favorite favorite) {
    favoriteRepository.addNewFavorite(favorite);
  }

  @DELETE
  @Path("/{id}")
  public void deleteFavorite(@PathParam("id") long id) {
    favoriteRepository.delete(id);
  }
}
