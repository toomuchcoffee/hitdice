package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.toomuchcoffee.hitdice.domain.world.Direction.*;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DungeonServiceTest {

    private DungeonService dungeonService;

    private Dungeon dungeon;

    private final Dungeon.Tile[][] tiles = new Dungeon.Tile[][]{
            {new Dungeon.Tile(ROOM), new Dungeon.Tile(ROOM), new Dungeon.Tile(ROOM)},
            {new Dungeon.Tile(ROOM), new Dungeon.Tile(ROOM), new Dungeon.Tile(ROOM)},
            {new Dungeon.Tile(ROOM), new Dungeon.Tile(ROOM), new Dungeon.Tile(ROOM)}
    };

    @BeforeEach
    void setUp() throws Exception {
        dungeonService = new DungeonService(null, null);
        dungeon = new Dungeon(tiles);
        dungeon.setPosition(Position.of(1, 1));
    }

    @Test
    void exploreNorth() {
        dungeonService.move(dungeon, NORTH);

        assertThat(dungeon.getPosition()).isEqualTo(Position.of(0, 1));
    }

    @Test
    void exploreSouth() {
        dungeonService.move(dungeon, SOUTH);

        assertThat(dungeon.getPosition()).isEqualTo(Position.of(2, 1));
    }

    @Test
    void exploreEast() {
        dungeonService.move(dungeon, EAST);

        assertThat(dungeon.getPosition()).isEqualTo(Position.of(1, 2));
    }

    @Test
    void exploreWest() {
        dungeonService.move(dungeon, WEST);

        assertThat(dungeon.getPosition()).isEqualTo(Position.of(1, 0));
    }
}