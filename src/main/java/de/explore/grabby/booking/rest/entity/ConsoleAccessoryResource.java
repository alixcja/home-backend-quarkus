package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.repository.entity.ConsoleAccessoryRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/accessories")
public class ConsoleAccessoryResource {

  @Inject
  ConsoleAccessoryRepository consoleAccessoryRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ConsoleAccessory> getAllConsoleAccessories() {
    return consoleAccessoryRepository.getAllConsoleAccessories();
  }

  @RolesAllowed("${admin-role}")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public Response addConsoleAccessory(@Valid @NotNull ConsoleAccessory consoleAccessory) {
    long id = consoleAccessoryRepository.persistConsoleAccessory(consoleAccessory);
    return Response.status(Response.Status.CREATED).entity(id).build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public void updateConsoleAccessory(@PathParam("id") long id, @Valid @NotNull ConsoleAccessory consoleAccessory) {
    ensureConsoleAccessoryExists(id);
    consoleAccessoryRepository.updateConsoleAccessory(id, consoleAccessory);
  }

  private void ensureConsoleAccessoryExists(long id) {
    Optional<ConsoleAccessory> byId = consoleAccessoryRepository.findByIdOptional(id);
    if (byId.isEmpty()) {
      throw new NotFoundException("Console Accessory with id " + id + " was not found");
    }
  }
}
