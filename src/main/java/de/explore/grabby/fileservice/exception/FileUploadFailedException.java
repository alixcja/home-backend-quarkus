package de.explore.grabby.fileservice.exception;

public class FileUploadFailedException extends RuntimeException {
  public FileUploadFailedException(Exception e) {
    super(e);
  }
}
