package de.explore.grabby.lunch.rest;

import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.repository.MenuCardRepository;
import de.explore.grabby.lunch.repository.ShopRepository;
import de.explore.grabby.lunch.rest.request.MenuUploadForm;
import de.explore.grabby.lunch.service.MenuCardService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.Optional;

@Path("/shops/{id}/menucards")
public class MenuCardResource {

  @Inject
  MenuCardRepository menucardRepository;

  @Inject
  ShopRepository shopRepository;

  @Inject
  MenuCardService menuCardService;

  @GET
  @Path("{number}")
  @Operation(summary = "Get menuCard by number", description = "Get menuCard by number for a shop")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Got menuCard by number for shops successfully",
                  content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM)),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response getMenuCardsForShopAndNumber(@PathParam("id") Long id, @PathParam("number") Long number) {
    ensureShopByIdExists(id);
    ResponseInputStream<GetObjectResponse> response = menuCardService.getImageForMenuCard(id, number);
    if (response == null) {
      Response.noContent().build();
    }
    return Response.ok(response).build();
  }

  @PUT
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Operation(summary = "Upload menuCards", description = "Uploads the menuCards for a shop")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "MenuCards for shop uploaded successfully"),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  public void uploadAndUpdateMenuCardsForShop(@PathParam("id") Long shopId, @Valid MenuUploadForm menuCard) {
    ensureShopByIdExists(shopId);
    menucardRepository.handleMenuCard(shopId, menuCard);
  }

  @DELETE
  public void deleteAllMenuCardsForShop(@PathParam("id") Long shopId) {
    ensureShopByIdExists(shopId);
    menucardRepository.deleteMenuCardsByShop(shopId);
  }

  private void ensureShopByIdExists(long id) {
    Optional<Shop> byIdOptional = shopRepository.findByIdOptional(id);
    if (byIdOptional.isEmpty()) {
      throw new NotFoundException("Could not find shop by id " + id);
    }
  }
}
