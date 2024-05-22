package de.explore.grabby.booking.model.entity;

import jakarta.persistence.Column;

public class ConsoleAccessory extends BookingEntity {

    @Column(name = "consoleType")
    private String consoleType;

    public String getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(String consoleType) {
        this.consoleType = consoleType;
    }
}
