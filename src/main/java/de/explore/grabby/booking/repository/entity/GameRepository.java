package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Game;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {

  public void persistGame(Game gameToPersist) {
    persist(gameToPersist);
  }

  public List<Game> getAllGames() {
    return listAll();
  }

  public void updateGame(long id, Game game) {
    Game gameToUpdate = findById(id);
    if (gameToUpdate != null) {
      gameToUpdate.setName(game.getName());
      gameToUpdate.setDescription(game.getDescription());
      gameToUpdate.setConsoleType(game.getConsoleType());
      persist(gameToUpdate);
    }
  }
}
