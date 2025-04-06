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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Path("/consoles")
@Tag(name = "Console", description = "Operations related to game consoles")
public class ConsoleResource {

    @Inject
    ConsoleRepository consoleRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all consoles", description = "Returns a list of all game consoles")
    @APIResponse(responseCode = "200", description = "Successfully retrieved all consoles",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Console.class)))
    public List<Console> getAllConsoles() {
        return consoleRepository.getAllConsoles();
    }

    @RolesAllowed("${admin-role}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Add a new console", description = "Adds a new console to the database")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Console created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
            @APIResponse(responseCode = "400", description = "Invalid input")
    })
    public Response addConsole(@Valid @NotNull Console console) {
        long id = consoleRepository.persistConsole(console);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @RolesAllowed("${admin-role}")
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Update console", description = "Updates the details of an existing console")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Console updated successfully"),
            @APIResponse(responseCode = "404", description = "Console with the provided ID not found")
    })
    @Parameter(name = "id", description = "ID of the console to be updated", required = true)
    public void updateConsole(@PathParam("id") long id, @Valid @NotNull Console console) {
        consoleRepository.updateConsole(id, console);
    }

    public void ensureConsoleExists(long id) {
        Optional<Console> byId = consoleRepository.findByIdOptional(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Console with id " + id + " was not found");
        }
    }
}
