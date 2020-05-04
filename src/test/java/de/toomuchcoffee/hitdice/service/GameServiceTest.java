package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.db.Hero;
import de.toomuchcoffee.hitdice.db.HeroRepository;
import de.toomuchcoffee.hitdice.db.Item;
import de.toomuchcoffee.hitdice.domain.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;

import static com.google.common.collect.Sets.newHashSet;
import static de.toomuchcoffee.hitdice.domain.event.factory.ArmorFactory.LEATHER;
import static de.toomuchcoffee.hitdice.domain.event.factory.WeaponFactory.LONGSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
    @Mock
    private HeroRepository heroRepository;

    private GameService gameService;

    @Before
    public void setUp() throws Exception {
        ItemMapper itemMapper = new ItemMapper();
        HeroMapper heroMapper = new HeroMapper(itemMapper);
        gameService = new GameService(heroRepository, heroMapper);
    }

    @Test
    public void savesGame() {
        de.toomuchcoffee.hitdice.domain.Hero hero = getHero();

        gameService.save(hero);

        ArgumentCaptor<Hero> argumentCaptor = ArgumentCaptor.forClass(Hero.class);
        verify(heroRepository).save(argumentCaptor.capture());

        Hero expected = getGame();

        assertThat(argumentCaptor.getValue()).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void restoresGame() {
        when(heroRepository.findById(123)).thenReturn(Optional.of(getGame()));

        de.toomuchcoffee.hitdice.domain.Hero hero = gameService.restore(123);

        de.toomuchcoffee.hitdice.domain.Hero expected = getHero();

        assertThat(hero).isEqualToIgnoringGivenFields(expected, "combatActions", "equipment");
        assertThat(hero.getEquipment()).hasSize(2);
        assertThat(hero.getEquipment()).contains(LEATHER.createObject(), LONGSWORD.createObject());
        assertThat(hero.getHealth().getValue()).isEqualTo(5);
        assertThat(hero.getHealth().getMaxValue()).isEqualTo(12);
    }

    private de.toomuchcoffee.hitdice.domain.Hero getHero() {
        de.toomuchcoffee.hitdice.domain.Hero hero = TestData.getHero();
        ReflectionTestUtils.setField(hero.getHealth(), "value", 5);
        hero.addEquipment(LONGSWORD.createObject());
        hero.addEquipment(LEATHER.createObject());
        hero.setLevel(2);
        hero.setExperience(251);
        hero.setName("Alrik");
        return hero;
    }

    private Hero getGame() {
        Hero expected = new Hero();
        expected.setStamina(12);
        expected.setDexterity(11);
        expected.setStrength(10);
        expected.setMaxHealth(12);
        expected.setHealth(5);
        expected.setItems(getItems());
        expected.setLevel(2);
        expected.setExperience(251);
        expected.setName("Alrik");
        return expected;
    }

    private HashSet<Item> getItems() {
        return newHashSet(
                createItem(LONGSWORD.name()),
                createItem(LEATHER.name())
        );
    }

    private Item createItem(String name) {
        Item item = new Item();
        item.setName(name);
        return item;
    }
}