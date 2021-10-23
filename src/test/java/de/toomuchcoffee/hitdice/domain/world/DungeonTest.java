package de.toomuchcoffee.hitdice.domain.world;


import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import org.junit.jupiter.api.Test;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.SOIL;
import static org.assertj.core.api.Assertions.assertThat;

class DungeonTest {

    @Test
    void viewMapShowsHeroSymbol() {
        Dungeon dungeon = new Dungeon(new Tile[][]{{new Tile(ROOM)}});
        dungeon.setPosition(Position.of(0, 0));
        String[][] map = dungeon.getMap();

        assertThat(map[0][0]).isEqualTo("user-circle hero");
    }

    @Test
    void getFlattenedTiles() {
        Tile[][] tiles = new Tile[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tiles[i][j] = new Tile(SOIL);
            }
        }
        Dungeon dungeon = new Dungeon(tiles);

        assertThat(dungeon.getFlattenedTiles()).hasSize(10 * 10);
    }
}