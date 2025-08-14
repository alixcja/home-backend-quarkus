package de.explore.grabby.booking.rest.entity;

public enum BookingEntityStatus {
  STATUS_ARCHIVED("archived"),
  STATUS_UNARCHIVED("unarchived");

  public final String label;

  BookingEntityStatus(String label) {
    this.label = label;
  }
}
