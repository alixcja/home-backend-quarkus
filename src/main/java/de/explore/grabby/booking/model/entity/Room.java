package de.explore.grabby.booking.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("room")
public class Room extends BookingEntity {

    public Room() {
        super();
    }
}
