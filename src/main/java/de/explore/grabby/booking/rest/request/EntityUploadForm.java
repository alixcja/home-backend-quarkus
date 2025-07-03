package de.explore.grabby.booking.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import java.io.File;

public class EntityUploadForm {
  @NotNull(message = "File must be set")
  @RestForm("file")
  @PartType(MediaType.APPLICATION_OCTET_STREAM)
  public File file;
}