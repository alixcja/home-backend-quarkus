package de.explore.grabby.model.entity;

import jakarta.persistence.Entity;

@Entity
public class ConsoleAccessory extends BookingEntity {
    private Console consoleType;

    public Console getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(Console consoleType) {
        this.consoleType = consoleType;
    }
}
