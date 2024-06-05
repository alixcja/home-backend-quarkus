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
        gameRepository.persist(game);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Game getGameById(@PathParam("id") Long id) {
        return gameRepository.findById(id);
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> getAllGames() {
        return gameRepository.listAll();
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateGameById(@PathParam("id") long id, Game game) {
        gameRepository.updateGame(id, game);
    }

    @Path("/archive/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void archiveGame(@PathParam("id") long id) {
        gameRepository.archiveGame(id);
    }
}
