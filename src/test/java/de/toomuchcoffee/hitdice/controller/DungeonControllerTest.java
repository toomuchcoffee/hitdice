package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.domain.Dungeon;
import de.toomuchcoffee.hitdice.domain.Event;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Position;
import de.toomuchcoffee.hitdice.service.DungeonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static de.toomuchcoffee.hitdice.domain.Direction.SOUTH;
import static de.toomuchcoffee.hitdice.domain.EventType.TREASURE;
import static de.toomuchcoffee.hitdice.factories.TreasureFactory.SHORTSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DungeonController.class)
@RunWith(SpringRunner.class)
public class DungeonControllerTest {

    @MockBean
    private DungeonService dungeonService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void dungeonCreate() throws Exception {
        when(dungeonService.create(1)).thenReturn(new Dungeon(1));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", new Hero(10, 11, 12));

        this.mvc.perform(get("/dungeon/create/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/explore"))
                .andExpect(xpath("//pre[@id='dungeon-map']").string(
                        "+---+\n" +
                                "|(#)|\n" +
                                "+---+" ))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[1]").string("Strength: 10"))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[2]").string("Dexterity: 11"))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[3]").string("Stamina: 12/12"))
        ;
    }

    @Test
    public void dungeonExploreSouth() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(2);
        dungeon.setPosition(new Position(0, 0));
        session.setAttribute("dungeon", dungeon);

        when(dungeonService.explore(SOUTH, dungeon, hero)).thenCallRealMethod();

        this.mvc.perform(get("/dungeon/explore/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/explore"))
                .andExpect(xpath("//pre[@id='dungeon-map']").string(
                        "+------+\n" +
                                "|      |\n" +
                                "|(#)   |\n" +
                                "+------+" ))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[1]").string("Strength: 10"))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[2]").string("Dexterity: 11"))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[3]").string("Stamina: 12/12"))
        ;

        verify(dungeonService).explore(eq(SOUTH), eq(dungeon), eq(hero));
    }

    @Test
    public void dungeonTreasure() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);
        Dungeon dungeon = new Dungeon(2);
        dungeon.setPosition(new Position(0, 0));
        session.setAttribute("dungeon", dungeon);

        when(dungeonService.explore(SOUTH, dungeon, hero)).thenReturn(new Event(TREASURE, SHORTSWORD));

        this.mvc.perform(get("/dungeon/explore/SOUTH")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/treasure"))
                .andExpect(xpath("//div[@id='treasure-actions']/a[1]/@href").string("/dungeon/collect"))
                .andExpect(xpath("//div[@id='treasure-actions']/a[2]/@href").string("/dungeon/continue"))
        ;

        assertThat(session.getAttribute("treasure")).isEqualTo(SHORTSWORD);
    }

    @Test
    public void dungeonContinue() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Dungeon dungeon = new Dungeon(1);
        session.setAttribute("dungeon", dungeon);
        session.setAttribute("hero", new Hero(10, 11, 12));

        this.mvc.perform(get("/dungeon/continue")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/explore"))
                .andExpect(xpath("//pre[@id='dungeon-map']").string(
                        "+---+\n" +
                                "|(#)|\n" +
                                "+---+" ))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[1]").string("Strength: 10"))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[2]").string("Dexterity: 11"))
                .andExpect(xpath("//fieldset[@id='hero-stats']/ul/li[3]").string("Stamina: 12/12"))
        ;

        verify(dungeonService).markAsVisited(dungeon);
    }

}