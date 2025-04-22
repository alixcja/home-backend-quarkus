package de.explore.grabby.lunch.rest;

import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.repository.ShopRepository;
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

@Path("/shops")
@Tag(name = "Shops", description = "Operations related to shops")
public class ShopResource {

  @Inject
  ShopRepository shopRepository;

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

  private void ensureShopByIdExists(long id) {
    Optional<Shop> byIdOptional = shopRepository.findByIdOptional(id);
    if (byIdOptional.isEmpty()) {
      throw new NotFoundException("Could not find shop by id " + id);
    }
  }
}
