package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.repository.entity.ConsoleAccessoryRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response addConsoleAccessory(ConsoleAccessory consoleAccessory) {
        boolean creationWasSuccessful = consoleAccessoryRepository.persistConsoleAccessory(consoleAccessory);
        if (!creationWasSuccessful) {
            throw new BadRequestException("Invalid input");
        }
        return Response.status(Response.Status.CREATED).build();
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
