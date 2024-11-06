package de.explore.grabby.booking.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("console")
public class Console extends BookingEntity {

  public Console() {
    // default
  }

  public Console(String name, String description) {
    super(name, description, "console");
  }
}
