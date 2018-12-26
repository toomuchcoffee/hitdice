package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.domain.Dungeon;
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
                .andExpect(xpath("//dl[@id='hero-stats']/dt[1]").string("Strength"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[1]").string("10"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[2]").string("Dexterity"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[2]").string("11"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[3]").string("Stamina"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[3]").string("12"))
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

        //when(dungeonService.explore(SOUTH, dungeon, hero)).thenReturn(new Event(EMPTY));
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
                .andExpect(xpath("//dl[@id='hero-stats']/dt[1]").string("Strength"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[1]").string("10"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[2]").string("Dexterity"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[2]").string("11"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[3]").string("Stamina"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[3]").string("12"))
        ;

        verify(dungeonService).explore(eq(SOUTH), eq(dungeon), eq(hero));
    }

}