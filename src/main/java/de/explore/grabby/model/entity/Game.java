package de.explore.grabby.model.entity;

import jakarta.persistence.Entity;

@Entity
public class Game extends BookingEntity {
    private Console consoleType;

    public Game(Console consoleType) {
        super();
        this.consoleType = consoleType;
    }

    public Console getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(Console consoleType) {
        this.consoleType = consoleType;
    }
}
