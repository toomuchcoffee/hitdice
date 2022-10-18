package de.toomuchcoffee.hitdice.db;

import de.toomuchcoffee.hitdice.config.JpaConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.toomuchcoffee.hitdice.domain.Dice.D6;
import static de.toomuchcoffee.hitdice.domain.Dice.n;
import static de.toomuchcoffee.hitdice.domain.event.factory.ArmorFactory.LEATHER;
import static de.toomuchcoffee.hitdice.domain.event.factory.PotionFactory.HEALTH;
import static de.toomuchcoffee.hitdice.domain.event.factory.WeaponFactory.SHORTSWORD;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
class HeroRepositoryTest {
    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savesGame() {
        Hero hero = new Hero();
        hero.setName("foo");
        Hero save = heroRepository.save(hero);

        List<Item> items = new ArrayList<Item>();
        items.add(ItemTestData.createItem(
                SHORTSWORD.getDisplayName(),
                SHORTSWORD.ordinal(),
                SHORTSWORD.isMetallic(),
                ItemType.WEAPON,
                Map.of("damage", D6.serialize())));
        items.add(ItemTestData.createItem(
                LEATHER.getDisplayName(),
                LEATHER.ordinal(),
                LEATHER.isMetallic(),
                ItemType.ARMOR,
                Map.of("protection", LEATHER.getProtection())));
        items.add(ItemTestData.createItem("health potion",
                HEALTH.ordinal(),
                false,
                ItemType.POTION,
                Map.of("potency", n(2).D(4).serialize())));
        items.forEach(i -> i.setHero(hero));
        hero.setItems(items);
        heroRepository.save(hero);

        Hero found = heroRepository.findById(save.getId())
                .orElseThrow(IllegalStateException::new);

        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("foo");
        assertThat(found.getItems()).isEqualTo(items);
        assertThat(found.getCreated()).isNotNull();

        List<Item> foundItems = entityManager.getEntityManager()
                .createQuery("SELECT i FROM Item i", Item.class).getResultList();
        assertThat(foundItems).hasSize(3);
    }

}