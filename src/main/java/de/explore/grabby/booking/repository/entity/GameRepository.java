package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.Game;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {
    public void updateGame(long id, Game game) {
        Game gameToUpdate = findById(id);
        if (gameToUpdate != null) {
            gameToUpdate.setName(game.getName());
            gameToUpdate.setDescription(game.getDescription());
            gameToUpdate.setConsoleType(game.getConsoleType());
            persist(gameToUpdate);
        }
    }

    public void archiveGame(long id) {
        Game gameToArchive = findById(id);
        if (gameToArchive != null) {
            gameToArchive.setArchived(!gameToArchive.getArchived());
            persist(gameToArchive);
        }
    }
}
