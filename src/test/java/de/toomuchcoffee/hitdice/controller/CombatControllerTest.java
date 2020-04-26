package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
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
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.*;
import static java.lang.String.format;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CombatController.class, secure = false)
@RunWith(SpringRunner.class)
public class CombatControllerTest {
    private static final String CAUSED_DAMAGE_MESSAGE = "%s hit %s with their %s for %d points of damage.";
    private final Tile[][] tiles = {{new Tile(ROOM)}};

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
        session.setAttribute("dungeon", new Dungeon(tiles));
        session.setAttribute("hero", hero);
        session.setAttribute("monster", monster);

        when(combatService.fight(eq(hero), eq(monster), anyInt()))
                .thenReturn(new CombatRound(0, new ArrayList<>(), ONGOING));

        this.mvc.perform(get("/combat/0")
                .secure(true)
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("combat"))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//div[@id='combat-stats']/div[1]/span").string("Your health: 12/12"))
                .andExpect(xpath("//div[@id='combat-stats']/div[2]/span").string("Orc's health: 7/7"))
                .andExpect(xpath("//div[@id='combat-actions']/a[1]/@href").string("/combat/1"))
                .andExpect(xpath("//div[@id='combat-actions']/a[2]/@href").string("/combat/flee"))
        ;
    }

    @Test
    public void combatAttack() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(tiles));
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
                                format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", "longsword", 3),
                                format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", "mace", 2)),
                        ONGOING
                ));

        this.mvc.perform(get("/combat/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("combat"))
                .andExpect(xpath("//div[@id='combat-round']/h5").string("Round 1:"))
                .andExpect(xpath("//div[@id='combat-round']/ul/li[1]").string("Alrik hit Orc with their longsword for 3 points of damage."))
                .andExpect(xpath("//div[@id='combat-round']/ul/li[2]").string("Orc hit Alrik with their mace for 2 points of damage."))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//div[@id='combat-stats']/div[1]/span").string("Your health: 10/12"))
                .andExpect(xpath("//div[@id='combat-stats']/div[2]/span").string("Orc's health: 4/7"))
                .andExpect(xpath("//div[@id='combat-actions']/a[1]/@href").string("/combat/2"))
                .andExpect(xpath("//div[@id='combat-actions']/a[2]/@href").string("/combat/flee"))
        ;
    }

    @Test
    public void combatAttackKillsMonster() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(tiles));
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
                                format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", "longsword", 7),
                                format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", "mace", 2)),
                        VICTORY
                ));

        this.mvc.perform(get("/combat/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("combat"))
                .andExpect(xpath("//div[@id='combat-round']/h5").string("Round 1:"))
                .andExpect(xpath("//div[@id='combat-round']//ul/li[1]").string("Alrik hit Orc with their longsword for 7 points of damage."))
                .andExpect(xpath("//div[@id='combat-round']//ul/li[2]").string("Orc hit Alrik with their mace for 2 points of damage."))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//div[@id='combat-stats']/div[1]/span").string("Your health: 10/12"))
                .andExpect(xpath("//div[@id='combat-stats']/div[2]/span").string("Orc's health: 0/7"))
                .andExpect(xpath("//div[@id='combat-actions']").doesNotExist())
                .andExpect(xpath("//div[@id='combat-victory']/p[1]").string("The Orc is dead!"))
                .andExpect(xpath("//div[@id='combat-victory']/p[2]").string("You earned 30 experience points!"))
                .andExpect(xpath("//div[@id='combat-victory']/a/@href").string("/combat/exit"))
        ;
    }

    @Test
    public void combatAttackKillsHero() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(tiles));
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
                                format(CAUSED_DAMAGE_MESSAGE, "Alrik", "Orc", "longsword", 2),
                                format(CAUSED_DAMAGE_MESSAGE, "Orc", "Alrik", "mace", 12)),
                        DEFEAT
                ));

        this.mvc.perform(get("/combat/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("combat"))
                .andExpect(xpath("//div[@id='combat-round']/h5").string("Round 1:"))
                .andExpect(xpath("//div[@id='combat-round']//ul/li[1]").string("Alrik hit Orc with their longsword for 2 points of damage."))
                .andExpect(xpath("//div[@id='combat-round']//ul/li[2]").string("Orc hit Alrik with their mace for 12 points of damage."))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//div[@id='combat-stats']/div[1]/span").string("Your health: 0/12"))
                .andExpect(xpath("//div[@id='combat-stats']/div[2]/span").string("Orc's health: 5/7"))
                .andExpect(xpath("//div[@id='combat-actions']").doesNotExist())
                .andExpect(xpath("//div[@id='combat-defeat']/p[1]").string("You are defeated!"))
                .andExpect(xpath("//div[@id='combat-defeat']/a/@href").string("/"))

        ;
    }

}