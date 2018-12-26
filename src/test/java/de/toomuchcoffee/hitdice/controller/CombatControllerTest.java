package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Dungeon;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static de.toomuchcoffee.hitdice.factories.TreasureFactory.CLUB;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CombatController.class)
@RunWith(SpringRunner.class)
public class CombatControllerTest {

    @MockBean
    private CombatService combatService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void dungeonReenterPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        session.setAttribute("hero", new Hero(10, 11, 12));

        this.mvc.perform(get("/combat/exit")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Explore dungeon")))
                .andExpect(content().string(containsString(
                        "<pre>" +
                                "+---+\n" +
                                "|(#)|\n" +
                                "+---+" +
                                "</pre>")))
                .andExpect(content().string(containsString("<dt>Strength</dt>")))
                .andExpect(content().string(containsString("<dd>10</dd>")))
        ;
    }

    @Test
    public void dungeonEnterCombatPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        session.setAttribute("hero", new Hero(10, 11, 12));
        session.setAttribute("monster", new Monster("Orc", 6, 7, CLUB, 15));

        this.mvc.perform(get("/combat/attack/0")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "<h3>Combat between you and <span>Orc</span></h3>\n" +
                                "\n" +
                                "        \n" +
                                "\n" +
                                "        <dl>\n" +
                                "            <dt>Your stamina:</dt>\n" +
                                "            <dd>\n" +
                                "                <span>12</span> /\n" +
                                "                <span>12</span>\n" +
                                "            </dd>\n" +
                                "            <dt><span>Orc</span>'s stamina:</dt>\n" +
                                "            <dd>\n" +
                                "                <span>7</span> /\n" +
                                "                <span>7</span>\n" +
                                "            </dd>\n" +
                                "        </dl>\n" +
                                "\n" +
                                "        <span>\n" +
                                "            <a href=\"/combat/attack/1\">Attack</a>\n" +
                                "            <a href=\"/combat/flee\">Flee</a>\n" +
                                "        </span>")))
        ;
    }

    @Test
    public void dungeonFightCombatPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);
        Monster monster = new Monster("Orc", 6, 7, CLUB, 15);
        session.setAttribute("monster", monster);

        when(combatService.attack(eq(hero), eq(monster))).thenReturn(7);
        when(combatService.attack(eq(monster), eq(hero))).thenReturn(5);

        this.mvc.perform(get("/combat/attack/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "<h3>Combat between you and <span>Orc</span></h3>\n" +
                                "\n" +
                                "        <span>\n" +
                                "            <div>\n" +
                                "                <h5>Round <span>1</span>:</h5>\n" +
                                "                <p>Your attack caused <span>7</span> points of damage.</p>\n" +
                                "                <p>You received <span>5</span> points of damage.</p>\n" +
                                "            </div>\n" +
                                "        </span>\n" +
                                "\n" +
                                "        <dl>\n" +
                                "            <dt>Your stamina:</dt>\n" +
                                "            <dd>\n" +
                                "                <span>12</span> /\n" +
                                "                <span>12</span>\n" +
                                "            </dd>\n" +
                                "            <dt><span>Orc</span>'s stamina:</dt>\n" +
                                "            <dd>\n" +
                                "                <span>7</span> /\n" +
                                "                <span>7</span>\n" +
                                "            </dd>\n" +
                                "        </dl>\n" +
                                "\n" +
                                "        <span>\n" +
                                "            <a href=\"/combat/attack/2\">Attack</a>\n" +
                                "            <a href=\"/combat/flee\">Flee</a>\n" +
                                "        </span>")))
        ;
    }

}