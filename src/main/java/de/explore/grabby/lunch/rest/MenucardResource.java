package de.explore.grabby.lunch.rest;

import de.explore.grabby.lunch.model.Menucard;
import de.explore.grabby.lunch.repository.MenucardRepository;
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

import static de.explore.grabby.lunch.util.ShopUtils.ensureShopByIdExists;

@Path("/shops/{id}")
public class MenucardResource {

  @Inject
  MenucardRepository menucardRepository;

  @GET
  @Operation(summary = "Get menucards", description = "Get all menucards for a shop")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Got menucards for shops successfully",
                  content = @Content(schema = @Schema(implementation = Menucard.class))),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  public List<Menucard> getMenucardsForId(@PathParam("id") Long id) {
    ensureShopByIdExists(id);
    return menucardRepository.findAllMenucardsByShop(id);
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Operation(summary = "Upload menucards", description = "Uploads the menucards for a shop")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "Menucards for shop uploaded successfully"),
          @APIResponse(responseCode = "404", description = "Shop with the provided ID not found")
  })
  @Parameter(name = "id", description = "ID of the shop to be updated", required = true)
  public void uploadMenucardsForShop(@PathParam("id") Long id, @Valid List<UploadForm> menucards) {
    ensureShopByIdExists(id);
    menucardRepository.uploadMenucards(id, menucards);
  }
}
