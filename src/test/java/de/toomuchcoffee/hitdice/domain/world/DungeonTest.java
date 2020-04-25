package de.toomuchcoffee.hitdice.domain.world;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DungeonTest {

    @Test
    public void viewMapShowsHeroSymbol() {
        Dungeon dungeon = new Dungeon(1);
        dungeon.setPosition(Position.of(0, 0));
        String[][] map = dungeon.getMap();

        assertThat(map[0][0]).isEqualTo("user-circle hero");
    }
}