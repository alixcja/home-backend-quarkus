package de.explore.grabby.booking.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("game")
public class Game extends BookingEntity {

    @Column(name = "consoleType")
    private String consoleType;

    public Game() {
        // default
    }

    public Game(String name, String description, String consoleType) {
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
