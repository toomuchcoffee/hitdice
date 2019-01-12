package de.toomuchcoffee.hitdice.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRepositoryTest {
    @Autowired
    private GameRepository gameRepository;

    @Test
    public void savesGame() {
        Game game = new Game();

        Game save = gameRepository.save(game);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getCreated()).isNotNull();
        assertThat(save.getModified()).isNull();
    }

    @Test
    public void modifiesGame() {
        Game save = gameRepository.save(new Game());

        Optional<Game> found = gameRepository.findById(save.getId());

        assertThat(found).isPresent();

        Game game = found.get();
        game.setName("foo");

        Game modified = gameRepository.save(game);

        assertThat(modified.getId()).isEqualTo(save.getId());
        assertThat(modified.getName()).isEqualTo("foo");
        assertThat(modified.getCreated()).isNotNull();
        assertThat(modified.getModified()).isNotNull();
        assertThat(modified.getModified()).isAfter(modified.getCreated());
    }
}