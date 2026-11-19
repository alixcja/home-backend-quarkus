package de.explore.grabby.fileservice.exception;

public class FileFetchFailedException extends RuntimeException {
  public FileFetchFailedException(Exception e) {
    super(e);
  }
}
