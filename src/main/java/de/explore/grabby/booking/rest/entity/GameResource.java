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

import java.util.List;

@Path("/games")
public class GameResource {

    @Inject
    GameRepository gameRepository;

    @RolesAllowed("${admin-role}")
    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addGame(@Valid @NotNull Game game) {
        boolean creationWasSuccessful = gameRepository.persistGame(game);
        if (!creationWasSuccessful) {
            throw new BadRequestException("Invalid input");
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @RolesAllowed("${admin-role}")
    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void updateGame(@PathParam("id") long id, @Valid @NotNull Game game) {
        gameRepository.updateGame(id, game);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> getAllGames() {
        return gameRepository.getAllGames();
    }
}
