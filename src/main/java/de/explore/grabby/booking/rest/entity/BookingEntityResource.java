package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/entities")
public class BookingEntityResource {

    @Inject
    BookingEntityRepository bookingEntityRepository;

    @Path("/{id}")
    @GET
    public BookingEntity findById(@PathParam("id") long id) {
        return bookingEntityRepository.findById(id);
    }

    @Path("/archive/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void archiveEntity(@PathParam("id") long id) {
        bookingEntityRepository.archiveEntityById(id);
    }

    // TODO - Split this into all entities, archived entities and not archvied entities
    @GET
    public List<BookingEntity> getAllEntities() {
        return bookingEntityRepository.listAll();
    }

    @POST
    @Path("/create/testdata")
    public void createTestBookingEntities() {
        createTestData();
    }

    @Transactional
    public void createTestData() {
        String nintendoSwitch = "Nintendo Switch";

        String description1 = "Mario Kart 8 Deluxe ist ein beliebtes Fun-Racing-Spiel für die Nintendo Switch. Es bietet rasante Rennen mit klassischen Nintendo-Charakteren wie Mario, Luigi, Bowser und vielen anderen. Die Spieler fahren in farbenfrohen Karts über fantasievolle Strecken und nutzen verschiedene Items wie Bananenschalen und Schildkrötenpanzer, um ihre Gegner auszubremsen.";
        Game game1 = new Game("Mario Kart 8 Deluxe", description1, nintendoSwitch);

        String description2 = "Super Smash Bros. Ultimate ist ein actiongeladenes Kampfspiel für die Nintendo Switch, in dem ikonische Charaktere aus verschiedenen Nintendo-Spielen und anderen Gaming-Franchises gegeneinander antreten. Es verfügt über das größte Kämpferaufgebot der Serie, darunter Mario, Link, Pikachu, und viele mehr, sowie Gastcharaktere wie Sonic, Pac-Man und sogar Figuren aus \"Final Fantasy\" und \"Street Fighter\". Die Spieler kämpfen auf interaktiven Plattformen, nutzen eine Vielzahl von Angriffen und Items und versuchen, ihre Gegner von der Stage zu schleudern.";
        Game game2 = new Game("Super Smash Bros Ultimate", description2, nintendoSwitch);


        Console console = new Console(nintendoSwitch, "rot-blau");

        ConsoleAccessory consoleAccessory = new ConsoleAccessory("Joycons", "blau-gelb", nintendoSwitch);

        bookingEntityRepository.persist(game1, game2, console, consoleAccessory);
    }
}
