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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CombatController.class)
@RunWith(SpringRunner.class)
public class CombatControllerTest {

    @MockBean
    private CombatService combatService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void combatStart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);
        Monster monster = new Monster("Orc", 6, 7, CLUB, 15);
        session.setAttribute("monster", monster);

        this.mvc.perform(get("/combat/attack/0")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/combat"))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//dl[@id='combat-stats']/dt[1]").string("Your stamina:"))
                .andExpect(xpath("//dl[@id='combat-stats']/dd[1]").string("12 / 12"))
                .andExpect(xpath("//dl[@id='combat-stats']/dt[2]").string("Orc's stamina:"))
                .andExpect(xpath("//dl[@id='combat-stats']/dd[2]").string("7 / 7"))
                .andExpect(xpath("//div[@id='combat-actions']/a[1]/@href").string("/combat/attack/1"))
                .andExpect(xpath("//div[@id='combat-actions']/a[2]/@href").string("/combat/flee"))
        ;

        verify(combatService, never()).attack(any(), any());
    }

    @Test
    public void combatAttack() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);
        Monster monster = new Monster("Orc", 6, 7, CLUB, 15);
        session.setAttribute("monster", monster);

        int damageCaused = 3;
        when(combatService.attack(eq(hero), eq(monster))).thenReturn(damageCaused);
        monster.decreaseCurrentStaminaBy(damageCaused);

        int damageReceived = 2;
        when(combatService.attack(eq(monster), eq(hero))).thenReturn(damageReceived);
        hero.decreaseCurrentStaminaBy(damageReceived);

        this.mvc.perform(get("/combat/attack/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/combat"))
                .andExpect(xpath("//div[@id='combat-round']/h5").string("Round 1:"))
                .andExpect(xpath("//div[@id='combat-round']/p[1]").string("Your attack caused 3 points of damage."))
                .andExpect(xpath("//div[@id='combat-round']/p[2]").string("You received 2 points of damage."))
                .andExpect(xpath("//div[@id='combat-actions']/a[2]/@href").string("/combat/flee"))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//dl[@id='combat-stats']/dt[1]").string("Your stamina:"))
                .andExpect(xpath("//dl[@id='combat-stats']/dd[1]").string("10 / 12"))
                .andExpect(xpath("//dl[@id='combat-stats']/dt[2]").string("Orc's stamina:"))
                .andExpect(xpath("//dl[@id='combat-stats']/dd[2]").string("4 / 7"))
                .andExpect(xpath("//div[@id='combat-actions']/a[1]/@href").string("/combat/attack/2"))
                .andExpect(xpath("//div[@id='combat-actions']/a[2]/@href").string("/combat/flee"))
        ;
    }

    @Test
    public void combatAttackKillsMonster() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);
        Monster monster = new Monster("Orc", 6, 7, CLUB, 15);
        session.setAttribute("monster", monster);

        doCallRealMethod().when(combatService).won(hero, monster);

        int damageCaused = 7;
        when(combatService.attack(eq(hero), eq(monster))).thenReturn(damageCaused);
        monster.decreaseCurrentStaminaBy(damageCaused);

        int damageReceived = 2;
        when(combatService.attack(eq(monster), eq(hero))).thenReturn(damageReceived);
        hero.decreaseCurrentStaminaBy(damageReceived);

        this.mvc.perform(get("/combat/attack/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/combat"))
                .andExpect(xpath("//div[@id='combat-round']/h5").string("Round 1:"))
                .andExpect(xpath("//div[@id='combat-round']/p[1]").string("Your attack caused 7 points of damage."))
                .andExpect(xpath("//div[@id='combat-round']/p[2]").string("You received 2 points of damage."))
                .andExpect(xpath("//h3").string("Combat between you and Orc"))
                .andExpect(xpath("//dl[@id='combat-stats']/dt[1]").string("Your stamina:"))
                .andExpect(xpath("//dl[@id='combat-stats']/dd[1]").string("10 / 12"))
                .andExpect(xpath("//dl[@id='combat-stats']/dt[2]").string("Orc's stamina:"))
                .andExpect(xpath("//dl[@id='combat-stats']/dd[2]").string("0 / 7"))
                .andExpect(xpath("//div[@id='combat-actions']").doesNotExist())
                .andExpect(xpath("//div[@id='combat-exit']/p[1]").string("The Orc is dead!"))
                .andExpect(xpath("//div[@id='combat-exit']/p[2]").string("You earned 15 experience points!"))
                .andExpect(xpath("//div[@id='combat-exit']/a/@href").string("/dungeon/continue"))
        ;

        assertThat(hero.getExperience()).isEqualTo(15);
    }

    @Test
    public void combatAttackKillsHero() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dungeon", new Dungeon(1));
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);
        Monster monster = new Monster("Orc", 6, 7, CLUB, 15);
        session.setAttribute("monster", monster);

        int damageCaused = 2;
        when(combatService.attack(eq(hero), eq(monster))).thenReturn(damageCaused);
        monster.decreaseCurrentStaminaBy(damageCaused);

        int damageReceived = 12;
        when(combatService.attack(eq(monster), eq(hero))).thenReturn(damageReceived);
        hero.decreaseCurrentStaminaBy(damageReceived);

        this.mvc.perform(get("/combat/attack/1")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("/dungeon/combat"))
                .andExpect(xpath("//div[@id='combat-round']").doesNotExist())
                .andExpect(xpath("//dl[@id='combat-stats']").doesNotExist())
                .andExpect(xpath("//div[@id='combat-actions']").doesNotExist())
                .andExpect(xpath("//div[@id='combat-exit']").doesNotExist())
                .andExpect(xpath("//p").string("You are dead!"))
        ;
    }

}