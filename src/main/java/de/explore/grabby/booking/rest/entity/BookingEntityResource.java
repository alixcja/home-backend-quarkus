package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import de.explore.grabby.booking.rest.request.UploadForm;
import de.explore.grabby.booking.service.BookingEntityService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.List;

@Path("/entities")
public class BookingEntityResource {

  @Inject
  BookingEntityRepository bookingEntityRepository;

  @Inject
  BookingEntityService bookingEntityService;

  @Path("/{id}")
  @GET
  @APIResponse(responseCode = "200", description = "Found entity by id")
  @APIResponse(responseCode = "404", description = "No entity found for provided id")
  public BookingEntity findById(@PathParam("id") long id) {
    return bookingEntityRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  @GET
  @APIResponse(responseCode = "200", description = "Got all entities")
  public List<BookingEntity> getAllEntities() {
    return bookingEntityRepository.listAll();
  }

  @Path("/archived")
  @GET
  @APIResponse(responseCode = "200", description = "Got all archived entities")
  public List<BookingEntity> getAllArchivedEntities() {
    return bookingEntityRepository.listAllArchived();
  }

  @Path("/not-archived")
  @GET
  @APIResponse(responseCode = "200", description = "Got all not archived entities")
  public List<BookingEntity> getAllNotArchivedEntities() {
    return bookingEntityRepository.listAllNotArchived();
  }

  // TODO - Only admins should be able to use this endpoint
  @POST
  @Path("/testdata")
  public void createTestBookingEntities() {
    createTestData();
  }

  @Path("/{id}/archive")
  @PUT
  @APIResponse(responseCode = "200", description = "Archived entity by id")
  @APIResponse(responseCode = "404", description = "No entity found for provided id")
  @Produces(MediaType.APPLICATION_JSON)
  public Response archiveEntity(@PathParam("id") long id) {
    ensureEntityExists(id);
    bookingEntityRepository.archiveEntityById(id);
    return Response.noContent().build();
  }

  @Path("/{id}/image")
  @GET
  @APIResponse(responseCode = "200", description = "Got image for entity with provided id")
  @APIResponse(responseCode = "204", description = "No image found for entity with provided id")
  @APIResponse(responseCode = "404", description = "No entity found for provided id")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response getImageForEntity(@PathParam("id") long id) {
    ensureEntityExists(id);
    ResponseInputStream<GetObjectResponse> response = bookingEntityService.getImageForEntity(id);
    if (response == null) {
      return Response.noContent().build();
    }
    return Response.ok(response).build();
  }

  @Path("/{id}/image")
  @PUT
  @APIResponse(responseCode = "200", description = "Uploaded image for entity with provided id")
  @APIResponse(responseCode = "400", description = "File is empty")
  @APIResponse(responseCode = "404", description = "No entity found for provided id")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void uploadImageForEntity(@PathParam("id") long id, UploadForm uploadForm) {
    ensureEntityExists(id);
    if (uploadForm.file == null) {
      throw new BadRequestException("File is empty");
    }
    bookingEntityService.uploadImageForEntity(id, uploadForm);
  }

  private void ensureEntityExists(Long id) {
    bookingEntityRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }


  // TODO - Admins should be able to persist their entities data

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
}
