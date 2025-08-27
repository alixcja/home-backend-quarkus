package de.explore.grabby.booking.repository.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.model.entity.Console;
import de.explore.grabby.booking.model.entity.Game;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class BookingEntityRepositoryTest {

  @Inject
  BookingEntityRepository bookingEntityRepository;

  private Game game;
  private Console console;

  @BeforeEach
  @Transactional
  void setUp() {
    game = new Game("Let's dance!", "This is a fun dance game", "Nintendo Switch");
    game.setIsArchived(true);
    console = new Console("Nintendo Switch", "blue-yellow");

    bookingEntityRepository.persist(game, console);
  }

  @Test
  void shouldArchiveEntity() {
    // when
    bookingEntityRepository.archiveEntityById(console.getId());

    // then
    Console persisted = (Console) bookingEntityRepository.findById(console.getId());
    assertTrue(persisted.getIsArchived());
  }

  @Test
  void shouldNotArchiveEntityDueInvalidEntityId() {
    // when / then
    assertThrows(NoSuchElementException.class, () -> bookingEntityRepository.archiveEntityById(999L));
  }

  @Test
  void shouldUnarchiveEntity() {
    // when
    bookingEntityRepository.unarchiveEntityById(game.getId());

    // then
    Game persisted = (Game) bookingEntityRepository.findById(game.getId());
    assertFalse(persisted.getIsArchived());
  }

  @Test
  void shouldNotUnarchiveEntityDueInvalidEntityId() {
    // when / then
    assertThrows(NoSuchElementException.class, () -> bookingEntityRepository.unarchiveEntityById(999L));
  }

  @Test
  void shouldReturnTwoNewlyAddedEntities() {
    // when
    List<BookingEntity> bookingEntities = bookingEntityRepository.listAllNewEntities();

    // then
    assertEquals(2, bookingEntities.size());
  }

  @AfterEach
  @Transactional
  void tearDown() {
    bookingEntityRepository.deleteAll();
  }
}