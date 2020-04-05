package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
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

import java.util.Optional;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.attribute.AttributeName.HEALTH;
import static de.toomuchcoffee.hitdice.domain.item.HandWeapon.SHORTSWORD;
import static de.toomuchcoffee.hitdice.domain.world.Direction.SOUTH;
import static org.assertj.core.api.Assertions.assertThat;
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

    @Before
    public void setUp() throws Exception {
        hero = TestData.getHero();
    }

    @Test
    public void dungeonCreate() throws Exception {
        when(random.nextInt(anyInt())).thenReturn(-4);
        when(dungeonService.create(1)).thenReturn(new Dungeon(1));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);

        this.mvc.perform(get("/dungeon/create/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/explore"))
        ;
    }

    @Test
    public void dungeonExploreSouth() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(2);
        dungeon.setPosition(new Position(0, 0));
        session.setAttribute("dungeon", dungeon);

        when(dungeonService.explore(SOUTH, dungeon)).thenCallRealMethod();

        this.mvc.perform(get("/dungeon/explore/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/explore"))
        ;

        verify(dungeonService).explore(eq(SOUTH), eq(dungeon));
    }

    @Test
    public void dungeonTreasure() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(2);
        dungeon.setPosition(new Position(0, 0));
        session.setAttribute("dungeon", dungeon);

        when(dungeonService.explore(SOUTH, dungeon)).thenReturn(Optional.of(SHORTSWORD));

        this.mvc.perform(get("/dungeon/explore/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/treasure"))
                .andExpect(xpath("//div[@id='treasure-actions']/a[1]/@href").string("/dungeon/collect"))
                .andExpect(xpath("//div[@id='treasure-actions']/a[2]/@href").string("/dungeon/leave"))
        ;

        assertThat(session.getAttribute("treasure")).isEqualTo(SHORTSWORD);
    }

    @Test
    public void dungeonPotion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(2);
        dungeon.setPosition(new Position(0, 0));
        session.setAttribute("dungeon", dungeon);

        Potion potion = new Potion(5, HEALTH);
        when(dungeonService.explore(SOUTH, dungeon)).thenReturn(Optional.of(potion));

        this.mvc.perform(get("/dungeon/explore/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/potion"))
                .andExpect(xpath("//h3").string("You found a health potion!"))
                .andExpect(xpath("//div[@id='potion-actions']/a[1]/@href").string("/dungeon/recover"))
                .andExpect(xpath("//div[@id='potion-actions']/a[2]/@href").string("/dungeon/leave"))
        ;

        assertThat(session.getAttribute("potion")).isEqualTo(potion);
    }

    @Test
    public void dungeonContinue() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Dungeon dungeon = new Dungeon(1);
        session.setAttribute("dungeon", dungeon);
        session.setAttribute("hero", hero);

        this.mvc.perform(get("/dungeon/continue")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/explore"))
        ;

        verify(dungeonService).markAsVisited(dungeon);
    }

}