package de.explore.grabby.fileservice;

import de.explore.grabby.fileservice.service.FileService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class FileServiceInitializer {
  private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

  @Inject
  FileService fileService;

  @ConfigProperty(name = "entity.bucket.name")
  String entityBucket;

  @ConfigProperty(name = "menuCard.bucket.name")
  String menuCardBucket;

  void onStart(@Observes StartupEvent ev) {
    LOG.info("Starting FileService initialization");
    fileService.ensureBucketExists(entityBucket);
    fileService.ensureBucketExists(menuCardBucket);
  }
}
