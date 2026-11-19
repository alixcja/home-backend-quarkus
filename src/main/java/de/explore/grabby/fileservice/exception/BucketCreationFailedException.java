package de.explore.grabby.fileservice.exception;

public class BucketCreationFailedException extends RuntimeException {
  public BucketCreationFailedException(Exception e) {
    super(e);
  }
}
