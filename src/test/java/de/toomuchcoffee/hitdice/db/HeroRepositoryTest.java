package de.toomuchcoffee.hitdice.db;

import de.toomuchcoffee.hitdice.config.JpaConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

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
        hero.setItems(new String[]{"SHORTSWORD", "LEATHER", "HEALTH"});

        Hero save = heroRepository.save(hero);
        Hero found = heroRepository.findById(save.getId())
                .orElseThrow(IllegalStateException::new);

        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("foo");
        assertThat(found.getItems()).isEqualTo(new String[]{"SHORTSWORD", "LEATHER", "HEALTH"});
        assertThat(found.getCreated()).isNotNull();
    }

}