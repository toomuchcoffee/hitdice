package de.toomuchcoffee.hitdice.domain.world;


import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import org.junit.Test;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static org.assertj.core.api.Assertions.assertThat;

public class DungeonTest {

    @Test
    public void viewMapShowsHeroSymbol() {
        Dungeon dungeon = new Dungeon(new Tile[][]{{new Tile(ROOM)}});
        dungeon.setPosition(Position.of(0, 0));
        String[][] map = dungeon.getMap();

        assertThat(map[0][0]).isEqualTo("user-circle hero");
    }
}