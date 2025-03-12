package de.explore.grabby.booking.rest;

import de.explore.grabby.booking.model.news.News;
import de.explore.grabby.booking.service.NewsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;

@Path("/news")
public class NewsResource {

  @Inject
  NewsService newsService;

  @Inject
  JsonWebToken jwt;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @APIResponse(responseCode = "200", description = "Got successfully bookings")
  public List<News> getNews() {
    return newsService.getAllCurrentNews(jwt.getSubject());
  }
}
