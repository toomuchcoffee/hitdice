package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Dungeon;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Position;
import org.junit.Before;
import org.junit.Test;

import static de.toomuchcoffee.hitdice.domain.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DungeonServiceTest {

    private DungeonService dungeonService;

    private Hero hero;
    private Dungeon dungeon;

    @Before
    public void setUp() throws Exception {
        dungeonService = new DungeonService();
        hero = new Hero(10, 11, 12);
        dungeon = new Dungeon(3);
        dungeon.setPosition(new Position(1, 1));
    }

    @Test
    public void shouldMoveNorth() {
        dungeonService.explore(NORTH, dungeon, hero);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(1, 0));
    }

    @Test
    public void shouldMoveSouth() {
        dungeonService.explore(SOUTH, dungeon, hero);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(1, 2));
    }

    @Test
    public void shouldMoveEast() {
        dungeonService.explore(EAST, dungeon, hero);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(2, 1));
    }

    @Test
    public void shouldMoveWest() {
        dungeonService.explore(WEST, dungeon, hero);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(0, 1));
    }
}