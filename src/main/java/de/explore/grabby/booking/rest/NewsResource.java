package de.explore.grabby.booking.rest;

import de.explore.grabby.booking.model.news.News;
import de.explore.grabby.booking.service.NewsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/news")
@Tag(name = "News", description = "Access current news related to bookings or platform updates")
@Produces(MediaType.APPLICATION_JSON)
public class NewsResource {

  @Inject
  NewsService newsService;

  @Inject
  JsonWebToken jwt;

  @GET
  @Operation(summary = "Get current news", description = "Retrieves all current news relevant to the authenticated user.")
  @APIResponse(
          responseCode = "200",
          description = "News successfully retrieved",
          content = @Content(schema = @Schema(implementation = News[].class))
  )
  public List<News> getNews() {
    return newsService.getAllCurrentNews(jwt.getSubject());
  }
}
