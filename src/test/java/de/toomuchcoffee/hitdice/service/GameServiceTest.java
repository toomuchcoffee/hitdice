package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.db.*;
import de.toomuchcoffee.hitdice.domain.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.toomuchcoffee.hitdice.domain.Dice.D8;
import static de.toomuchcoffee.hitdice.domain.Dice.n;
import static de.toomuchcoffee.hitdice.domain.event.factory.ArmorFactory.LEATHER;
import static de.toomuchcoffee.hitdice.domain.event.factory.PotionFactory.HEALTH;
import static de.toomuchcoffee.hitdice.domain.event.factory.WeaponFactory.LONGSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private HeroRepository heroRepository;

    private GameService gameService;

    @BeforeEach
    void setUp() throws Exception {
        ItemMapper itemMapper = new ItemMapper();
        HeroMapper heroMapper = new HeroMapper(itemMapper);
        gameService = new GameService(heroRepository, heroMapper);
    }

    @Test
    void savesGame() {
        de.toomuchcoffee.hitdice.domain.Hero hero = getHero();

        gameService.save(hero);

        ArgumentCaptor<Hero> argumentCaptor = ArgumentCaptor.forClass(Hero.class);
        verify(heroRepository).save(argumentCaptor.capture());

        Hero expected = getGame();

        assertThat(argumentCaptor.getValue()).usingRecursiveComparison().ignoringFields("items").isEqualTo(expected);
        assertThat(argumentCaptor.getValue().getItems()).containsAll(getItems());
    }

    @Test
    void restoresGame() {
        when(heroRepository.findById(123)).thenReturn(Optional.of(getGame()));

        de.toomuchcoffee.hitdice.domain.Hero hero = gameService.restore(123);

        de.toomuchcoffee.hitdice.domain.Hero expected = getHero();

        assertThat(hero).usingRecursiveComparison().ignoringFields("combatActions", "equipment").isEqualTo(expected);
        assertThat(hero.getEquipment()).hasSize(3);
        assertThat(hero.getEquipment().stream()
                .map(de.toomuchcoffee.hitdice.domain.equipment.Item::getDisplayName)
                .collect(Collectors.toSet())).contains("leather armor", "longsword", "health potion");
        assertThat(hero.getHealth().getValue()).isEqualTo(5);
        assertThat(hero.getHealth().getMaxValue()).isEqualTo(12);
    }

    private de.toomuchcoffee.hitdice.domain.Hero getHero() {
        de.toomuchcoffee.hitdice.domain.Hero hero = TestData.getHero();
        ReflectionTestUtils.setField(hero.getHealth(), "value", 5);
        hero.addEquipment(LONGSWORD.create());
        hero.addEquipment(LEATHER.create());
        hero.addEquipment(HEALTH.create());
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

    private List<Item> getItems() {
        return List.of(
                ItemTestData.createItem(
                        LONGSWORD.getDisplayName(),
                        LONGSWORD.ordinal(),
                        LONGSWORD.isMetallic(),
                        ItemType.WEAPON,
                        Map.of("damage", D8.serialize())
                ),
                ItemTestData.createItem(
                        LEATHER.getDisplayName(),
                        LEATHER.ordinal(),
                        LEATHER.isMetallic(),
                        ItemType.ARMOR,
                        Map.of("protection", LEATHER.getProtection())),
                ItemTestData.createItem("health potion",
                        HEALTH.ordinal(),
                        false,
                        ItemType.POTION,
                        Map.of(
                                "potency", n(2).D(4).serialize(),
                                "type", HEALTH.name()
                        ))
        );
    }

}