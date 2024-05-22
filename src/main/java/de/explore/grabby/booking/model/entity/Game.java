package de.explore.grabby.booking.model.entity;

import jakarta.persistence.Column;

public class Game extends BookingEntity {

    @Column(name = "consoleType")
    private String consoleType;

    public Game() {
        // default constructor
    }

    public Game(String consoleType) {
        super();
        this.consoleType = consoleType;
    }

    public String getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(String consoleType) {
        this.consoleType = consoleType;
    }
}
