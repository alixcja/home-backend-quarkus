package de.explore.grabby.lunch.service;

import de.explore.grabby.fileservice.service.FileService;
import de.explore.grabby.lunch.model.MenuCard;
import de.explore.grabby.lunch.repository.MenuCardRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;

@ApplicationScoped
public class MenuCardService {

  @ConfigProperty(name = "menuCard.bucket.name")
  String bucket;

  @Inject
  MenuCardRepository menuCardRepository;

  @Inject
  FileService fileService;

  // TODO: persist bucket also in db
  public InputStream getImageForMenuCard(long id, Long number) {
    MenuCard menuCard = menuCardRepository.findByShopAndNumber(id, number).orElseThrow();
    return fileService.getImage(bucket, menuCard.getFileName());
  }
}
