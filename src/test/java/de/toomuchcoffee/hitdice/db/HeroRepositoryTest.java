package de.toomuchcoffee.hitdice.db;

import com.google.common.collect.ImmutableMap;
import de.toomuchcoffee.hitdice.config.JpaConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;
import static de.toomuchcoffee.hitdice.domain.Dice.n;
import static de.toomuchcoffee.hitdice.domain.event.factory.ArmorFactory.LEATHER;
import static de.toomuchcoffee.hitdice.domain.event.factory.PotionFactory.HEALTH;
import static de.toomuchcoffee.hitdice.domain.event.factory.WeaponFactory.SHORTSWORD;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@RunWith(SpringRunner.class)
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
public class HeroRepositoryTest {
    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savesGame() {
        Hero hero = new Hero();
        hero.setName("foo");
        Hero save = heroRepository.save(hero);

        List<Item> items = newArrayList(
                ItemTestData.createItem(
                        SHORTSWORD.getDisplayName(),
                        SHORTSWORD.ordinal(),
                        SHORTSWORD.isMetallic(),
                        ItemType.WEAPON,
                        ImmutableMap.of("damage", D6.serialize())
                ),
                ItemTestData.createItem(
                        LEATHER.getDisplayName(),
                        LEATHER.ordinal(),
                        LEATHER.isMetallic(),
                        ItemType.ARMOR,
                        ImmutableMap.of("protection", LEATHER.getProtection())),
                ItemTestData.createItem("health potion",
                        HEALTH.ordinal(),
                        false,
                        ItemType.POTION,
                        ImmutableMap.of("potency", n(2).D(4).serialize()))
        );
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