package de.explore.grabby.booking.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("accessory")
public class ConsoleAccessory extends BookingEntity {

    @Column(name = "consoleType")
    private String consoleType;

    public ConsoleAccessory() {
        // default
    }

    public ConsoleAccessory(String name, String description, String consoleType) {
        super(name, description, "game");
        this.consoleType = consoleType;
    }

    public String getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(String consoleType) {
        this.consoleType = consoleType;
    }
}
