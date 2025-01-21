package de.explore.grabby.booking.model.entity.embedded;

import jakarta.persistence.Embeddable;

@Embeddable
public class Image {

  private String filename;

  private String bucket;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getBucket() {
    return bucket;
  }

  public void setBucket(String bucket) {
    this.bucket = bucket;
  }
}
