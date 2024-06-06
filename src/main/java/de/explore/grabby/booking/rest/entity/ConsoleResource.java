package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.repository.entity.ConsoleRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/console")
public class ConsoleResource {

    @Inject
    ConsoleRepository consoleRepository;

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addConsole(Console console) {
        consoleRepository.persist(console);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Console getConsoleById(@PathParam("id") Long id) {
        return consoleRepository.findById(id);
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Console> getAllConsoles() {
        return consoleRepository.listAll();
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateConsoleById(@PathParam("id") long id, Console console) {
        consoleRepository.updateConsole(id, console);
    }

    @Path("/archive/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void archiveConsole(@PathParam("id") long id) {
        consoleRepository.archiveConsole(id);
    }
}
