package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.GameRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/game")
public class GameResource {

    @Inject
    GameRepository gameRepository;

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addGame(Game game) {
        gameRepository.persistGame(game);
    }

    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void updateGame(@PathParam("id") long id, Game game) {
        gameRepository.updateGame(id, game);
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> getAllGames() {
        return gameRepository.getAllGames();
    }
}
