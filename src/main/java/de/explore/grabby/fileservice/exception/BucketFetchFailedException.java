package de.explore.grabby.fileservice.exception;

public class BucketFetchFailedException extends RuntimeException {
  public BucketFetchFailedException(Exception e) {
    super(e);
  }
}
