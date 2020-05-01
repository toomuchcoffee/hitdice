package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.service.CombatService;
import de.toomuchcoffee.hitdice.service.CombatService.CombatRound;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static de.toomuchcoffee.hitdice.domain.combat.HandWeapon.LONGSWORD;
import static de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate.ORC;
import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.*;
import static java.lang.String.format;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CombatController.class, secure = false)
@RunWith(SpringRunner.class)
public class CombatControllerTest {
    private static final String CAUSED_DAMAGE_MESSAGE = "%s hit %s with their %s for %d points of damage.";

    @MockBean
    private CombatService combatService;

    @Autowired
    private MockMvc mvc;

    private Hero hero;
    private Monster monster;

    @Before
    public void setUp() throws Exception {
        hero = TestData.getHero();
        hero.setName("Alrik");
        hero.addEquipment(LONGSWORD);
        monster = new Monster(ORC);
        ReflectionTestUtils.setField(monster, "health", new Health(7));
    }

    @Test
    public void combatStart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        CombatRound combatRound = new CombatRound(0, new ArrayList<>(), ONGOING);
        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(combatRound);

        this.mvc.perform(get("/combat/0")
                .secure(true)
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("modal", "combat"))
                .andExpect(flash().attribute("round", combatRound.getRound()))
                .andExpect(flash().attribute("events", combatRound.getEvents()))
                .andExpect(flash().attribute("result", combatRound.getResult()))
                .andExpect(flash().attribute("monster", monster))
        ;
    }

    @Test
    public void combatAttack() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        CombatRound combatRound = new CombatRound(
                1,
                newArrayList(
                        format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", "longsword", 3),
                        format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", "mace", 2)),
                ONGOING
        );
        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(combatRound);

        this.mvc.perform(get("/combat/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("modal", "combat"))
                .andExpect(flash().attribute("round", combatRound.getRound()))
                .andExpect(flash().attribute("events", combatRound.getEvents()))
                .andExpect(flash().attribute("result", combatRound.getResult()))
                .andExpect(flash().attribute("monster", monster))
        ;
    }

    @Test
    public void combatAttackKillsMonster() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        CombatRound combatRound = new CombatRound(
                1,
                newArrayList(
                        format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", "longsword", 7),
                        format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", "mace", 2)),
                VICTORY
        );
        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(combatRound);

        this.mvc.perform(get("/combat/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("modal", "combat"))
                .andExpect(flash().attribute("round", combatRound.getRound()))
                .andExpect(flash().attribute("events", combatRound.getEvents()))
                .andExpect(flash().attribute("result", combatRound.getResult()))
                .andExpect(flash().attribute("monster", monster))
        ;
    }

    @Test
    public void combatAttackKillsHero() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        CombatRound combatRound = new CombatRound(
                1,
                newArrayList(
                        format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", "longsword", 2),
                        format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", "mace", 12)),
                DEFEAT
        );
        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(combatRound);

        this.mvc.perform(get("/combat/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("modal", "combat"))
                .andExpect(flash().attribute("round", combatRound.getRound()))
                .andExpect(flash().attribute("events", combatRound.getEvents()))
                .andExpect(flash().attribute("result", combatRound.getResult()))
                .andExpect(flash().attribute("monster", monster))
        ;
    }

}