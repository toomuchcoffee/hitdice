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
public class GameRepositoryTest {
    @Autowired
    private GameRepository gameRepository;

    @Test
    public void savesGame() {
        Game game = new Game();
        game.setName("foo");
        game.setItems(new String[]{"SHORTSWORD", "LEATHER", "HEALTH"});

        Game save = gameRepository.save(game);
        Game found = gameRepository.findById(save.getId())
                .orElseThrow(IllegalStateException::new);

        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("foo");
        assertThat(found.getItems()).isEqualTo(new String[]{"SHORTSWORD", "LEATHER", "HEALTH"});
        assertThat(found.getCreated()).isNotNull();
    }

}