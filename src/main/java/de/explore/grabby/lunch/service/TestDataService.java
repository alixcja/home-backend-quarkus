package de.explore.grabby.lunch.service;

import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.ConsoleAccessory;
import de.explore.grabby.booking.model.entity.Game;
import de.explore.grabby.booking.repository.entity.ConsoleAccessoryRepository;
import de.explore.grabby.booking.repository.entity.ConsoleRepository;
import de.explore.grabby.booking.repository.entity.GameRepository;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TestDataService {
  private static final Logger LOG = LoggerFactory.getLogger(TestDataService.class);

  @Inject
  ConsoleRepository consoleRepository;

  @Inject
  GameRepository gameRepository;

  @Inject
  ConsoleAccessoryRepository consoleAccessoryRepository;

  @Transactional
  @Startup
  public void onStart() {
    if (isDevMode() && isDatabaseEmpty()) {
      LOG.info("Application was started in dev mode and database was empty - initializing test data");

      Console nintendoSwitch = new Console("Nintendo Switch", "Die Nintendo Switch ist eine Hybrid-Spielkonsole von Nintendo, die sowohl am Fernseher als auch mobil genutzt werden kann.");
      Console playstation4 = new Console("Playstation 4", "Die PlayStation 4 (PS4) ist eine Spielkonsole von Sony, die leistungsstarkes Gaming und Multimedia-Funktionen bietet.");
      consoleRepository.persist(nintendoSwitch, playstation4);

      ConsoleAccessory nsJoysticksBlauGelb = new ConsoleAccessory("Joysticks blau / gelb", "Die Joy-Cons sind die abnehmbaren Controller der Nintendo Switch und ermöglichen flexibles Spielen allein oder zu zweit.", "Nintendo Switch");
      ConsoleAccessory nsJoysticksRotBlau = new ConsoleAccessory("Joysticks rot / blau", "Die Joy-Cons sind die abnehmbaren Controller der Nintendo Switch und ermöglichen flexibles Spielen allein oder zu zweit.", "Nintendo Switch");
      ConsoleAccessory dualShock4  = new ConsoleAccessory("DualShock 4 ", "Der DualShock 4 ist der offizielle Controller der PS4, mit präziser Steuerung, Touchpad und eingebautem Lautsprecher.", "Playstation 4");
      consoleAccessoryRepository.persist(nsJoysticksRotBlau, nsJoysticksBlauGelb, dualShock4);

      Game theLegendOfZeldaBreathOfTheWild = new Game("The Legend of Zelda: Breath of the Wild", "Ein offenes Abenteuer voller Entdeckungen in einer riesigen Fantasy-Welt.", "Nintendo Switch");
      Game marioKart8Deluxe = new Game("Mario Kart 8 Deluxe", "Rasante Kart-Rennen mit beliebten Nintendo-Charakteren und verrückten Strecken.", "Nintendo Switch");
      Game theLastOfUsPartII = new Game("The Last of Us Part II", "Ein emotionales Action-Abenteuer in einer postapokalyptischen Welt.", "Playstation 4");
      Game godOfWar = new Game("The Last of Us Part II", "Ein episches Abenteuer um den Krieger Kratos und seinen Sohn in der nordischen Mythologie.", "Playstation 4");
      gameRepository.persist(theLastOfUsPartII, theLegendOfZeldaBreathOfTheWild, marioKart8Deluxe, godOfWar);
    }
  }

  private boolean isDatabaseEmpty() {
    int gameSize = gameRepository.listAll().size();
    int consoleSize = consoleRepository.listAll().size();
    int consoleAccessory = consoleAccessoryRepository.listAll().size();
    return gameSize == 0 && consoleSize == 0 && consoleAccessory == 0;
  }

  private boolean isDevMode() {
    return LaunchMode.current().equals(LaunchMode.DEVELOPMENT);
  }
}
