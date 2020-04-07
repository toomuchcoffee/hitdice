package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static de.toomuchcoffee.hitdice.domain.world.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DungeonServiceTest {

    private DungeonService dungeonService;

    private Dungeon dungeon;

    @Before
    public void setUp() throws Exception {
        dungeonService = new DungeonService(null, null, null, null);
        dungeon = new Dungeon(3);
        dungeon.setPosition(new Position(1, 1));
    }

    @Test
    public void exploreNorth() {
        dungeonService.move(dungeon, NORTH);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(0, 1));
    }

    @Test
    public void exploreSouth() {
        dungeonService.move(dungeon, SOUTH);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(2, 1));
    }

    @Test
    public void exploreEast() {
        dungeonService.move(dungeon, EAST);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(1, 2));
    }

    @Test
    public void exploreWest() {
        dungeonService.move(dungeon, WEST);

        assertThat(dungeon.getPosition()).isEqualTo(new Position(1, 0));
    }
}