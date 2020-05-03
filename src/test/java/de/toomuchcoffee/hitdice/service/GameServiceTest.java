package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.db.Game;
import de.toomuchcoffee.hitdice.db.GameRepository;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static de.toomuchcoffee.hitdice.domain.equipment.Armor.LEATHER;
import static de.toomuchcoffee.hitdice.domain.equipment.HandWeapon.LONGSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    private GameService gameService;

    @Before
    public void setUp() throws Exception {
        gameService = new GameService(gameRepository);
    }

    @Test
    public void savesGame() {
        Hero hero = getHero();

        gameService.save(hero);

        ArgumentCaptor<Game> argumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(argumentCaptor.capture());

        Game expected = getGame();

        assertThat(argumentCaptor.getValue()).isEqualToIgnoringGivenFields(expected, "items");
        assertThat(argumentCaptor.getValue().getItems()).hasSize(2);
        assertThat(argumentCaptor.getValue().getItems()).contains("LONGSWORD", "LEATHER");
    }

    @Test
    public void restoresGame() {
        when(gameRepository.getOne(123)).thenReturn(getGame());

        Hero hero = gameService.restore(123);

        Hero expected = getHero();

        assertThat(hero).isEqualToIgnoringGivenFields(expected, "combatActions", "equipment");
        assertThat(hero.getEquipment()).hasSize(2);
        assertThat(hero.getEquipment()).contains(LEATHER, LONGSWORD);
        assertThat(hero.getHealth().getValue()).isEqualTo(5);
        assertThat(hero.getHealth().getMaxValue()).isEqualTo(12);
    }

    private Hero getHero() {
        Hero hero = TestData.getHero();
        ReflectionTestUtils.setField(hero.getHealth(), "value", 5);
        hero.addEquipment(LONGSWORD);
        hero.addEquipment(LEATHER);
        hero.setLevel(2);
        hero.setExperience(251);
        hero.setName("Alrik");
        return hero;
    }

    private Game getGame() {
        Game expected = new Game();
        expected.setStamina(12);
        expected.setDexterity(11);
        expected.setStrength(10);
        expected.setMaxHealth(12);
        expected.setHealth(5);
        expected.setItems(new String[]{"LONGSWORD", "LEATHER"});
        expected.setLevel(2);
        expected.setExperience(251);
        expected.setName("Alrik");
        return expected;
    }
}