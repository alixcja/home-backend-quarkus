package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.repository.entity.ConsoleAccessoryRepository;
import io.quarkus.runtime.util.StringUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

import static de.explore.grabby.booking.rest.entity.BookingEntityStatus.STATUS_ARCHIVED;
import static de.explore.grabby.booking.rest.entity.BookingEntityStatus.STATUS_UNARCHIVED;

@Path("/accessories")
@Tag(name = "Console Accessory", description = "Operations related to console accessories")
public class ConsoleAccessoryResource {

  @Inject
  ConsoleAccessoryRepository consoleAccessoryRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get all console accessories", description = "Returns a list of all console accessories")
  @APIResponse(responseCode = "200", description = "Successfully retrieved all console accessories",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsoleAccessory[].class)))
  public List<ConsoleAccessory> getAllConsoleAccessories(@QueryParam("status") String status) {
    if (StringUtil.isNullOrEmpty(status)) {
      return consoleAccessoryRepository.listAll();
    } else if (status.equals(STATUS_ARCHIVED.label)) {
      return consoleAccessoryRepository.listAllArchived();
    } else if (status.equals(STATUS_UNARCHIVED.label)) {
      return consoleAccessoryRepository.listAllNotArchived();
    }
    throw new BadRequestException("Unknown status type");
  }

  @RolesAllowed("${admin-role}")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  @Operation(summary = "Add a new console accessory", description = "Adds a new console accessory to the database")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "Console accessory created successfully",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
          @APIResponse(responseCode = "400", description = "Invalid input")
  })
  public Response addConsoleAccessory(@Valid @NotNull ConsoleAccessory consoleAccessory) {
    long id = consoleAccessoryRepository.persistConsoleAccessory(consoleAccessory);
    return Response.status(Response.Status.CREATED).entity(id).build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  @Operation(summary = "Update console accessory", description = "Updates the details of an existing console accessory")
  @APIResponses({
          @APIResponse(responseCode = "204", description = "Console accessory updated successfully"),
          @APIResponse(responseCode = "404", description = "Console accessory with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the console accessory to be updated", required = true)
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
