package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.event.Event;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.domain.world.Position;
import de.toomuchcoffee.hitdice.service.DungeonService;
import de.toomuchcoffee.hitdice.service.HeroService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
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

@WebMvcTest(controllers = DungeonController.class, secure = false)
@RunWith(SpringRunner.class)
public class DungeonControllerTest {

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

    @Before
    public void setUp() throws Exception {
        hero = TestData.getHero();
    }

    @Test
    public void dungeonCreate() throws Exception {
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
    public void dungeonExploreSouth() throws Exception {
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
    public void dungeonTreasure() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(TILES);
        dungeon.setPosition(Position.of(0, 0));
        session.setAttribute("dungeon", dungeon);

        Tile tile = new Tile(ROOM);
        Event<HandWeapon> event = SHORTSWORD.createEvent();
        tile.setEvent(event);
        when(dungeonService.move(dungeon, SOUTH)).thenReturn(tile);

        this.mvc.perform(get("/dungeon/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dungeon"))
                .andExpect(flash().attribute("treasure", event.getObject()));
        ;
    }

    @Test
    public void dungeonPotion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(TILES);
        dungeon.setPosition(Position.of(0, 0));
        session.setAttribute("dungeon", dungeon);

        Tile tile = new Tile(ROOM);
        Event<Potion> event = HEALTH.createEvent();
        tile.setEvent(event);
        when(dungeonService.move(dungeon, SOUTH)).thenReturn(tile);

        this.mvc.perform(get("/dungeon/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dungeon"))
                .andExpect(flash().attribute("treasure", event.getObject()));
    }

}