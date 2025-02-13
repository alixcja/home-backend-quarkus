package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import de.explore.grabby.booking.rest.request.UploadForm;
import de.explore.grabby.booking.service.BookingEntityService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

  @RolesAllowed("${admin-role}")
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

  @RolesAllowed("${admin-role}")
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
      response = bookingEntityService.getDefaultEntityImage();
    }
    return Response.ok(response).build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}/image")
  @PUT
  @APIResponse(responseCode = "200", description = "Uploaded image for entity with provided id")
  @APIResponse(responseCode = "400", description = "File is empty")
  @APIResponse(responseCode = "404", description = "No entity found for provided id")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void uploadImageForEntity(@PathParam("id") long id, @Valid @NotNull UploadForm uploadForm) {
    ensureEntityExists(id);
    bookingEntityService.uploadImageForEntity(id, uploadForm);
  }

  private void ensureEntityExists(Long id) {
    bookingEntityRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }
}
