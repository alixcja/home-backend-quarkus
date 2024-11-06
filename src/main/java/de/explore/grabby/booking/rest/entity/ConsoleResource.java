package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.repository.entity.ConsoleRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/consoles")
public class ConsoleResource {

    @Inject
    ConsoleRepository consoleRepository;

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addConsole(Console console) {
        consoleRepository.persistConsole(console);
    }

    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void updateConsole(@PathParam("id") long id, Console console) {
        consoleRepository.updateConsole(id, console);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Console> getAllConsoles() {
        return consoleRepository.getAllConsoles();
    }
}
