package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.repository.entity.ConsoleRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/consoles")
public class ConsoleResource {

    @Inject
    ConsoleRepository consoleRepository;

    @RolesAllowed("${admin-role}")
    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addConsole(@Valid @NotNull Console console) {
        consoleRepository.persistConsole(console);
        return Response.status(Response.Status.CREATED).build();
    }

    @RolesAllowed("${admin-role}")
    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void updateConsole(@PathParam("id") long id, @Valid @NotNull Console console) {
        consoleRepository.updateConsole(id, console);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Console> getAllConsoles() {
        return consoleRepository.getAllConsoles();
    }
}
