package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.domain.world.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.toomuchcoffee.hitdice.domain.world.Direction.*;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DungeonExplorationServiceTest {

    private DungeonExplorationService dungeonExplorationService;
    private Dungeon dungeon;

    private final Tile[][] tiles = new Tile[][]{
            {new Tile(Position.of(0, 0), ROOM), new Tile(Position.of(1, 0), ROOM), new Tile(Position.of(2, 0), ROOM)},
            {new Tile(Position.of(0, 1), ROOM), new Tile(Position.of(1, 1), ROOM), new Tile(Position.of(2, 1), ROOM)},
            {new Tile(Position.of(0, 2), ROOM), new Tile(Position.of(1, 2), ROOM), new Tile(Position.of(2, 2), ROOM)},
    };

    @BeforeEach
    void setUp() throws Exception {
        dungeonExplorationService = new DungeonExplorationService();
        dungeon = new Dungeon(tiles);
        dungeon.setPosition(Position.of(1, 1));
    }

    @Test
    void exploreNorth() {
        dungeonExplorationService.move(dungeon, NORTH);
        assertThat(dungeon.getPosition()).isEqualTo(Position.of(0, 1));
    }

    @Test
    void exploreSouth() {
        dungeonExplorationService.move(dungeon, SOUTH);
        assertThat(dungeon.getPosition()).isEqualTo(Position.of(2, 1));
    }

    @Test
    void exploreEast() {
        dungeonExplorationService.move(dungeon, EAST);
        assertThat(dungeon.getPosition()).isEqualTo(Position.of(1, 2));
    }

    @Test
    void exploreWest() {
        dungeonExplorationService.move(dungeon, WEST);
        assertThat(dungeon.getPosition()).isEqualTo(Position.of(1, 0));
    }
}