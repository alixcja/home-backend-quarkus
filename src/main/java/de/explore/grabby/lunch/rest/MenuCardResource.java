package de.explore.grabby.lunch.rest;

import de.explore.grabby.lunch.model.MenuCard;
import de.explore.grabby.lunch.model.Shop;
import de.explore.grabby.lunch.repository.MenuCardRepository;
import de.explore.grabby.lunch.repository.ShopRepository;
import de.explore.grabby.lunch.rest.request.UploadForm;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;
import java.util.Optional;

@Path("/shops/{id}/menucards")
public class MenuCardResource {

  @Inject
  MenuCardRepository menucardRepository;

  @Inject
  ShopRepository shopRepository;

  @GET
  @Operation(summary = "Get menuCards", description = "Get all menuCards for a shop")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Got menuCards for shops successfully",
                  content = @Content(schema = @Schema(implementation = MenuCard.class))),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  public List<MenuCard> getMenuCardsForId(@PathParam("id") Long id) {
    ensureShopByIdExists(id);
    return menucardRepository.findAllMenuCardsByShop(id);
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Operation(summary = "Upload menuCards", description = "Uploads the menuCards for a shop")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "MenuCards for shop uploaded successfully"),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  public void uploadAndPersistMenuCardsForShop(@PathParam("id") Long id, @Valid UploadForm menuCard) {
    ensureShopByIdExists(id);
    menucardRepository.uploadAndPersist(id, menuCard);
  }

  @PUT
  @Path("/{menuCardId}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Operation(summary = "Upload menuCards", description = "Uploads the menuCards for a shop")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "MenuCards for shop uploaded successfully"),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  public void uploadAndUpdateMenuCardsForShop(@PathParam("id") Long id, @PathParam("menuCardId") Long menuCardId, @Valid UploadForm menuCard) {
    ensureShopByIdExists(id);
    ensureMenuCardWithNumberExists(menuCardId);
    menucardRepository.uploadAndUpdate(menuCardId, menuCard);
  }

  private void ensureMenuCardWithNumberExists(Long menuCardId) {
    Optional<MenuCard> byIdOptional = menucardRepository.findByIdOptional(menuCardId);
    if (byIdOptional.isEmpty()) {
      throw new NotFoundException("Could not find menuCard by id " + menuCardId);
    }
  }

  private void ensureShopByIdExists(long id) {
    Optional<Shop> byIdOptional = shopRepository.findByIdOptional(id);
    if (byIdOptional.isEmpty()) {
      throw new NotFoundException("Could not find shop by id " + id);
    }
  }
}
