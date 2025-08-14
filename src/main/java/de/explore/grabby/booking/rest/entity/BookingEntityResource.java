package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import de.explore.grabby.booking.rest.request.EntityUploadForm;
import de.explore.grabby.booking.service.BookingEntityService;
import io.quarkus.runtime.util.StringUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.List;

import static de.explore.grabby.booking.rest.entity.BookingEntityStatus.STATUS_ARCHIVED;
import static de.explore.grabby.booking.rest.entity.BookingEntityStatus.STATUS_UNARCHIVED;

@Path("/entities")
@Tag(name = "Booking Entities", description = "Operations related to booking entities")
public class BookingEntityResource {

  // TODO: Add here post endpoint?
  @Inject
  BookingEntityRepository bookingEntityRepository;

  @Inject
  BookingEntityService bookingEntityService;

  @Path("/{id}")
  @GET
  @Operation(summary = "Find booking entity by ID", description = "Returns the booking entity associated with the provided ID")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Found entity by id",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingEntity.class))),
          @APIResponse(responseCode = "404", description = "No entity found for provided id")
  })
  @Parameter(name = "id", description = "ID of the booking entity", required = true)
  public BookingEntity findById(@PathParam("id") long id) {
    return bookingEntityRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  @GET
  @Operation(summary = "Get all booking entities", description = "Returns a list of all booking entities. Can filter by status.")
  @APIResponse(responseCode = "200", description = "Got all entities",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingEntity[].class)))
  @Parameter(name = "status", description = "Status filter for entities (archived or unarchived)")
  public List<BookingEntity> getAllEntities(@QueryParam("status") String status) {
    if (StringUtil.isNullOrEmpty(status)) {
      return bookingEntityRepository.listAll();
    } else if (status.equals(STATUS_ARCHIVED.label)) {
      return bookingEntityRepository.listAllArchived();
    } else if (status.equals(STATUS_UNARCHIVED.label)) {
      return bookingEntityRepository.listAllNotArchived();
    }
    throw new BadRequestException("Unknown status type");
  }

  @Path("/{id}/image")
  @GET
  @Operation(summary = "Get image for entity", description = "Returns the image associated with the booking entity by ID")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Got image for entity with provided id",
                  content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM)),
          @APIResponse(responseCode = "404", description = "No entity found for provided id")
  })
  @Parameter(name = "id", description = "ID of the booking entity to fetch image", required = true)
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response getImageForEntity(@PathParam("id") long id) {
    ensureEntityExists(id);
    ResponseInputStream<GetObjectResponse> response = bookingEntityService.getImageForEntity(id);
    if (response == null) {
      response = bookingEntityService.getDefaultEntityImage();
    }
    return Response.ok(response).build();
  }

  @RolesAllowed("${admin-role}")
  @POST
  @Operation(summary = "Create a new booking entity", description = "Create a new booking entity with the provided details")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "Entity was created",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingEntity.class))),
          @APIResponse(responseCode = "400", description = "Invalid input")
  })
  @Parameter(name = "entity", description = "Booking entity to be created", required = true)
  public Response persistEntity(@Valid @NotNull BookingEntity entity) {
    bookingEntityRepository.persist(entity);
    return Response.status(201).build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}/archive")
  @PUT
  @Operation(summary = "Archive a booking entity", description = "Archives the booking entity with the provided ID")
  @APIResponses({
          @APIResponse(responseCode = "204", description = "Archived entity by id"),
          @APIResponse(responseCode = "404", description = "No entity found for provided id")
  })
  @Produces(MediaType.APPLICATION_JSON)
  @Parameter(name = "id", description = "ID of the booking entity to be archived", required = true)
  public Response archiveEntity(@PathParam("id") long id) {
    ensureEntityExists(id);
    bookingEntityRepository.archiveEntityById(id);
    return Response.noContent().build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}/unarchive")
  @PUT
  @Operation(summary = "Unarchive a booking entity", description = "Unarchives the booking entity with the provided ID")
  @APIResponses({
          @APIResponse(responseCode = "204", description = "Unarchived entity by id"),
          @APIResponse(responseCode = "404", description = "No entity found for provided id")
  })
  @Produces(MediaType.APPLICATION_JSON)
  @Parameter(name = "id", description = "ID of the booking entity to be unarchived", required = true)
  public Response unarchiveEntity(@PathParam("id") long id) {
    ensureEntityExists(id);
    bookingEntityRepository.unarchiveEntityById(id);
    return Response.noContent().build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}/image")
  @PUT
  @Operation(summary = "Upload image for a booking entity", description = "Uploads an image for the booking entity with the provided ID")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Uploaded image for entity with provided id"),
          @APIResponse(responseCode = "400", description = "File is empty"),
          @APIResponse(responseCode = "404", description = "No entity found for provided id")
  })
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Parameter(name = "id", description = "ID of the booking entity to upload the image for", required = true)
  @Parameter(name = "uploadForm", description = "The file upload form with the image", required = true)
  public Response uploadImageForEntity(@PathParam("id") long id, @Valid @NotNull EntityUploadForm uploadForm) {
    ensureEntityExists(id);
    bookingEntityService.uploadImageForEntity(id, uploadForm);
    return Response.status(HttpStatus.SC_CREATED).build();
  }


  @RolesAllowed("${admin-role}")
  @POST
  @Path("/testdata")
  @Operation(summary = "Create test data for booking entities", description = "Generates test data for booking entities")
  @APIResponse(responseCode = "204", description = "Test data created successfully")
  public void createTestBookingEntities() {
    createTestData();
  }

  @Transactional
  public void createTestData() {
    String nintendoSwitch = "Nintendo Switch";

    String description1 = "Mario Kart 8 Deluxe ist ein beliebtes Fun-Racing-Spiel für die Nintendo Switch. Es bietet rasante Rennen mit klassischen Nintendo-Charakteren wie Mario, Luigi, Bowser und vielen anderen. Die Spieler fahren in farbenfrohen Karts über fantasievolle Strecken und nutzen verschiedene Items wie Bananenschalen und Schildkrötenpanzer, um ihre Gegner auszubremsen.";
    Game game1 = new Game("Mario Kart 8 Deluxe", description1, nintendoSwitch);

    String description2 = "Super Smash Bros. Ultimate ist ein actiongeladenes Kampfspiel für die Nintendo Switch, in dem ikonische Charaktere aus verschiedenen Nintendo-Spielen und anderen Gaming-Franchises gegeneinander antreten. Es verfügt über das größte Kämpferaufgebot der Serie, darunter Mario, Link, Pikachu, und viele mehr, sowie Gastcharaktere wie Sonic, Pac-Man und sogar Figuren aus \"Final Fantasy\" und \"Street Fighter\". Die Spieler kämpfen auf interaktiven Plattformen, nutzen eine Vielzahl von Angriffen und Items und versuchen, ihre Gegner von der Stage zu schleudern.";
    Game game2 = new Game("Super Smash Bros Ultimate", description2, nintendoSwitch);


    Console console = new Console(nintendoSwitch, "rot-blau");

    ConsoleAccessory consoleAccessory = new ConsoleAccessory("Joycons", "blau-gelb", nintendoSwitch);

    bookingEntityRepository.persist(game1, game2, console, consoleAccessory);
  }

  private void ensureEntityExists(Long id) {
    bookingEntityRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }
}
