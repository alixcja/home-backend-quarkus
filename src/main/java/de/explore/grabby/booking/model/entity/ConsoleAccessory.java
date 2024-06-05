package de.explore.grabby.booking.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("consoleAccessory")
public class ConsoleAccessory extends BookingEntity {

    public ConsoleAccessory() {
        super();
    }

    @Column(name = "consoleType")
    private String consoleType;

    public String getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(String consoleType) {
        this.consoleType = consoleType;
    }
}
