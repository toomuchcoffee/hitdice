package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
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

import static de.toomuchcoffee.hitdice.domain.item.HandWeapon.CLUB;
import static de.toomuchcoffee.hitdice.service.CombatService.CAUSED_DAMAGE_MESSAGE;
import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CombatController.class, secure = false)
@RunWith(SpringRunner.class)
public class CombatControllerTest {

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
        monster = new Monster("Orc", 2, 0, 1, new WeaponAttack(CLUB));
        ReflectionTestUtils.setField(monster, "health", new Health(7));
    }

    @Test
    public void combatStart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(new CombatRound(0, new ArrayList<>(), ONGOING));

        this.mvc.perform(get("/combat/attack/0")
                .secure(true)
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/combat"))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//div[@id='combat-stats']/div[1]/span").string("Your health: 12/12"))
                .andExpect(xpath("//div[@id='combat-stats']/div[2]/span").string("Orc's health: 7/7"))
                .andExpect(xpath("//div[@id='combat-actions']/a[1]/@href").string("/combat/attack/1"))
                .andExpect(xpath("//div[@id='combat-actions']/a[2]/@href").string("/dungeon/flee"))
        ;
    }

    @Test
    public void combatAttack() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        int damageCaused = 3;
        monster.reduceHealth(damageCaused);

        int damageReceived = 2;
        hero.reduceHealth(damageReceived);

        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(new CombatRound(
                        1,
                        newArrayList(
                                format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", 3),
                                format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", 2)),
                        ONGOING
                ));

        this.mvc.perform(get("/combat/attack/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/combat"))
                .andExpect(xpath("//div[@id='combat-round']/h5").string("Round 1:"))
                .andExpect(xpath("//div[@id='combat-round']/ul/li[1]").string("Alrik hit Orc for 3 points of damage."))
                .andExpect(xpath("//div[@id='combat-round']/ul/li[2]").string("Orc hit Alrik for 2 points of damage."))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//div[@id='combat-stats']/div[1]/span").string("Your health: 10/12"))
                .andExpect(xpath("//div[@id='combat-stats']/div[2]/span").string("Orc's health: 4/7"))
                .andExpect(xpath("//div[@id='combat-actions']/a[1]/@href").string("/combat/attack/2"))
                .andExpect(xpath("//div[@id='combat-actions']/a[2]/@href").string("/dungeon/flee"))
        ;
    }

    @Test
    public void combatAttackKillsMonster() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        int damageCaused = 7;
        monster.reduceHealth(damageCaused);

        int damageReceived = 2;
        hero.reduceHealth(damageReceived);

        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(new CombatRound(
                        1,
                        newArrayList(
                                format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", 7),
                                format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", 2)),
                        VICTORY
                ));

        this.mvc.perform(get("/combat/attack/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/combat"))
                .andExpect(xpath("//div[@id='combat-round']/h5").string("Round 1:"))
                .andExpect(xpath("//div[@id='combat-round']//ul/li[1]").string("Alrik hit Orc for 7 points of damage."))
                .andExpect(xpath("//div[@id='combat-round']//ul/li[2]").string("Orc hit Alrik for 2 points of damage."))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//div[@id='combat-stats']/div[1]/span").string("Your health: 10/12"))
                .andExpect(xpath("//div[@id='combat-stats']/div[2]/span").string("Orc's health: 0/7"))
                .andExpect(xpath("//div[@id='combat-actions']").doesNotExist())
                .andExpect(xpath("//div[@id='combat-exit']/p[1]").string("The Orc is dead!"))
                .andExpect(xpath("//div[@id='combat-exit']/p[2]").string("You earned 30 experience points!"))
                .andExpect(xpath("//div[@id='combat-exit']/a/@href").string("/dungeon/continue"))
        ;

        assertThat(session.getAttribute("monster")).isNull();
    }

    @Test
    public void combatAttackKillsHero() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        int damageCaused = 2;
        monster.reduceHealth(damageCaused);

        int damageReceived = 12;
        hero.reduceHealth(damageReceived);

        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(new CombatRound(
                        1,
                        newArrayList(
                                format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", 2),
                                format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", 12)),
                        DEATH
                ));

        this.mvc.perform(get("/combat/attack/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("dungeon/dead"))
                .andExpect(xpath("//h2").string("You are dead!"))
                .andExpect(xpath("//div[@id='page_content']/a/@href").string("/"))
        ;
    }

}