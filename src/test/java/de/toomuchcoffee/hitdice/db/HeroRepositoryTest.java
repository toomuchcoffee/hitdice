package de.toomuchcoffee.hitdice.db;

import de.toomuchcoffee.hitdice.config.JpaConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
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

    @Test
    public void savesGame() {
        Hero hero = new Hero();
        hero.setName("foo");
        Hero save = heroRepository.save(hero);

        Set<Item> items = newHashSet(
                createItem(SHORTSWORD.name()),
                createItem(LEATHER.name()),
                createItem(HEALTH.name()));
        items.forEach(i -> i.setHero(hero));
        hero.setItems(items);
        heroRepository.save(hero);

        Hero found = heroRepository.findById(save.getId())
                .orElseThrow(IllegalStateException::new);

        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("foo");
        assertThat(found.getItems()).isEqualTo(items);
        assertThat(found.getCreated()).isNotNull();
    }

    private Item createItem(String name) {
        Item item = new Item();
        item.setName(name);
        return item;
    }

}