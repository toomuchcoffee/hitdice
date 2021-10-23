package de.toomuchcoffee.hitdice;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = DOCKER)
class ApplicationTest {

    @Test
    void startsUp() {
    }
}