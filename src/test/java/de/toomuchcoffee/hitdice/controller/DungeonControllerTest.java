package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.equipment.Weapon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.domain.world.Position;
import de.toomuchcoffee.hitdice.service.DungeonService;
import de.toomuchcoffee.hitdice.service.HeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.event.factory.PotionFactory.HEALTH;
import static de.toomuchcoffee.hitdice.domain.event.factory.WeaponFactory.SHORTSWORD;
import static de.toomuchcoffee.hitdice.domain.world.Direction.SOUTH;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DungeonController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class DungeonControllerTest {

    @MockBean
    private DungeonService dungeonService;

    @MockBean
    private HeroService heroService;

    @MockBean
    private Random random;

    @Autowired
    private MockMvc mvc;

    private Hero hero;

    private final Tile[][] TILES = new Tile[][]{
            {new Tile(ROOM), new Tile(ROOM), new Tile(ROOM)},
            {new Tile(ROOM), new Tile(ROOM), new Tile(ROOM)},
            {new Tile(ROOM), new Tile(ROOM), new Tile(ROOM)}
    };

    @BeforeEach
    void setUp() throws Exception {
        hero = TestData.getHero();
    }

    @Test
    void dungeonCreate() throws Exception {
        when(random.nextInt(anyInt())).thenReturn(-4);
        when(dungeonService.create(1)).thenReturn(new Dungeon(new Tile[][]{{new Tile(ROOM)}}));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);

        this.mvc.perform(get("/dungeon/enter")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dungeon"))
        ;
    }

    @Test
    void dungeonExploreSouth() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(TILES);
        dungeon.setPosition(Position.of(0, 0));
        session.setAttribute("dungeon", dungeon);

        when(dungeonService.move(dungeon, SOUTH)).thenCallRealMethod();

        this.mvc.perform(get("/dungeon/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dungeon"))
        ;

        verify(dungeonService).move(eq(dungeon), eq(SOUTH));
    }

    @Test
    void dungeonTreasure() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(TILES);
        dungeon.setPosition(Position.of(0, 0));
        session.setAttribute("dungeon", dungeon);

        Tile tile = new Tile(ROOM);
        Weapon object = SHORTSWORD.create();
        tile.setOccupant(object);
        when(dungeonService.move(dungeon, SOUTH)).thenReturn(tile);

        this.mvc.perform(get("/dungeon/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dungeon"))
                .andExpect(flash().attribute("treasure", object));
        ;
    }

    @Test
    void dungeonPotion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(TILES);
        dungeon.setPosition(Position.of(0, 0));
        session.setAttribute("dungeon", dungeon);

        Tile tile = new Tile(ROOM);
        Potion object = HEALTH.create();
        tile.setOccupant(object);
        when(dungeonService.move(dungeon, SOUTH)).thenReturn(tile);

        this.mvc.perform(get("/dungeon/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dungeon"))
                .andExpect(flash().attribute("treasure", object));
    }

}