package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.repository.entity.ConsoleAccessoryRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/accessory")
public class ConsoleAccessoryResource {

    @Inject
    ConsoleAccessoryRepository consoleAccessoryRepository;

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addConsoleAccessory(ConsoleAccessory consoleAccessory) {
        consoleAccessoryRepository.persist(consoleAccessory);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ConsoleAccessory getConsoleAccessoryById(@PathParam("id") Long id) {
        return consoleAccessoryRepository.findById(id);
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConsoleAccessory> getAllConsoleAccessorys() {
        return consoleAccessoryRepository.listAll();
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateConsoleAccessoryById(@PathParam("id") long id, ConsoleAccessory consoleAccessory) {
        consoleAccessoryRepository.updateConsoleAccessory(id, consoleAccessory);
    }

    @Path("/archive/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void archiveConsoleAccessory(@PathParam("id") long id) {
        consoleAccessoryRepository.archiveConsoleAccessory(id);
    }
}
