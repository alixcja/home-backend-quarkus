package de.explore.grabby.model.entity;

public class Game extends Entity {
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
