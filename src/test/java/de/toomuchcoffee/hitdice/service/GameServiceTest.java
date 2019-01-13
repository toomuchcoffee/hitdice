package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.db.Game;
import de.toomuchcoffee.hitdice.db.GameRepository;
import de.toomuchcoffee.hitdice.domain.Hero;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static de.toomuchcoffee.hitdice.domain.Armor.LEATHER;
import static de.toomuchcoffee.hitdice.domain.HandWeapon.LONGSWORD;
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

        assertThat(argumentCaptor.getValue()).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void restoresGame() {
        when(gameRepository.getOne(123)).thenReturn(getGame());

        Hero hero = gameService.restore(123);

        Hero expected = getHero();

        assertThat(hero).isEqualToIgnoringGivenFields(expected, "combatActions");
    }

    private Hero getHero() {
        Hero hero = new Hero(11, 12, 13, 13);
        hero.setHealth(5);
        hero.setWeapon(LONGSWORD);
        hero.setArmor(LEATHER);
        hero.setLevel(2);
        hero.setExperience(251);
        hero.setName("Alrik");
        return hero;
    }

    private Game getGame() {
        Game expected = new Game();
        expected.setStamina(13);
        expected.setDexterity(12);
        expected.setStrength(11);
        expected.setMaxHealth(13);
        expected.setHealth(5);
        expected.setWeapon(LONGSWORD);
        expected.setArmor(LEATHER);
        expected.setLevel(2);
        expected.setExperience(251);
        expected.setName("Alrik");
        return expected;
    }
}