package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.repository.entity.ConsoleAccessoryRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/accessories")
public class ConsoleAccessoryResource {

    @Inject
    ConsoleAccessoryRepository consoleAccessoryRepository;

    // TODO - Only Admins
    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addConsoleAccessory(ConsoleAccessory consoleAccessory) {
        consoleAccessoryRepository.persistConsoleAccessory(consoleAccessory);
    }

    // TODO - Only Admins
    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void updateConsoleAccessory(@PathParam("id") long id, ConsoleAccessory consoleAccessory) {
        consoleAccessoryRepository.updateConsoleAccessory(id, consoleAccessory);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConsoleAccessory> getAllConsoleAccessories() {
        return consoleAccessoryRepository.getAllConsoleAccessories();
    }
}
