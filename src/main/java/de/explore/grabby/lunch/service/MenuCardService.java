package de.explore.grabby.lunch.service;

import de.explore.grabby.fileservice.FileService;
import de.explore.grabby.lunch.model.MenuCard;
import de.explore.grabby.lunch.repository.MenuCardRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@ApplicationScoped
public class MenuCardService {

  @ConfigProperty(name = "menuCard.bucket.name")
  String bucket;

  @Inject
  MenuCardRepository menuCardRepository;

  @Inject
  FileService fileService;

  // TODO: persist bucket also in db
  public ResponseInputStream<GetObjectResponse> getImageForMenuCard(long id, Long number) {
    MenuCard menuCard = menuCardRepository.findByShopAndNumber(id, number).orElseThrow();
    return fileService.getImage(bucket, menuCard.getFileName());
  }
}
