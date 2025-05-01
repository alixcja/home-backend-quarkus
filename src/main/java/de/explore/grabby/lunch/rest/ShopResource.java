package de.explore.grabby.lunch.rest;

import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.repository.ShopRepository;
import de.explore.grabby.lunch.rest.request.UploadForm;
import de.explore.grabby.lunch.service.ShopService;
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
import java.util.Optional;

@Path("/shops")
@Tag(name = "Shops", description = "Operations related to shops")
public class ShopResource {

  @Inject
  ShopRepository shopRepository;

  @Inject
  ShopService shopService;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get shop by ID", description = "Retrieves a single shop by its unique ID.")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Shop retrieved successfully",
                  content = @Content(schema = @Schema(implementation = Shop.class))),
          @APIResponse(responseCode = "404", description = "No shop found for provided id")
  })
  public Response getShopByID(@PathParam("id") long id) {
    Shop byId = shopRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    return Response.ok(byId).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get shops", description = "Retrieves shops")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Shops retrieved successfully",
                  content = @Content(schema = @Schema(implementation = Shop[].class)))
  })
  public List<Shop> getShops() {
    return shopRepository.listAll();
  }

  @Transactional
  @RolesAllowed("${admin-role}")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Create a new shop entity", description = "Create a new shop entity with the provided details")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "Shop was created",
                  content = @Content(schema = @Schema(implementation = Shop.class))),
          @APIResponse(responseCode = "400", description = "Invalid input")
  })
  @Parameter(name = "entity", description = "Shop entity to be created", required = true)
  public Response persistEntity(@Valid @NotNull Shop shop) {
    shopRepository.persist(shop);
    return Response.status(Response.Status.CREATED)
            .entity(shop)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }

  @RolesAllowed("${admin-role}")
  @Path("/{id}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Update shop", description = "Updates the details of an existing shop")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Shop updated successfully"),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  public Response updateShop(@PathParam("id") long id, @Valid @NotNull Shop updatedShop) {
    ensureShopByIdExists(id);
    Shop updated = shopRepository.put(id, updatedShop);
    return Response.ok(updated).build();
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
    ensureShopByIdExists(id);
    ResponseInputStream<GetObjectResponse> response = shopService.getImageForEntity(id);
    if (response == null) {
      response = shopService.getDefaultEntityImage();
    }
    return Response.ok(response).build();
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
  public Response uploadImageForEntity(@PathParam("id") long id, @Valid @NotNull UploadForm uploadForm) {
    ensureShopByIdExists(id);
    shopService.uploadImageForEntity(id, uploadForm);
    return Response.status(HttpStatus.SC_CREATED).build();
  }


  private void ensureShopByIdExists(long id) {
    Optional<Shop> byIdOptional = shopRepository.findByIdOptional(id);
    if (byIdOptional.isEmpty()) {
      throw new NotFoundException("Could not find shop by id " + id);
    }
  }
}
