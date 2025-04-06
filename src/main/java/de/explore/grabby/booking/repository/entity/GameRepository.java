package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Game;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {

  private static final Logger LOG = LoggerFactory.getLogger(GameRepository.class);

  public long persistGame(Game game) {
    persist(game);
    LOG.info("New entity with id {} was persisted", game.getId());
    return game.getId();
  }

  public List<Game> getAllGames() {
    return listAll();
  }

  public void updateGame(long id, Game game) {
    Game gameToUpdate = findById(id);
    gameToUpdate.setName(game.getName());
    gameToUpdate.setDescription(game.getDescription());
    gameToUpdate.setConsoleType(game.getConsoleType());
    persist(gameToUpdate);
    LOG.info("Entity with id {} was updated", id);
  }
}
