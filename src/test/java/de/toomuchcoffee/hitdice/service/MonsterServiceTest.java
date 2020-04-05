package de.toomuchcoffee.hitdice.service;


import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Random;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonsterServiceTest {

    @Mock
    private Random random;

    private MonsterService monsterService;

    @Before
    public void setUp() throws Exception {
        monsterService = new MonsterService(random);
    }

    @Test
    public void createsTheRightMonster() {
        when(random.nextInt(7)).thenReturn(0, 3, 4, 5, 6);

        List<MonsterTemplate> monsterTemplates = newArrayList(
                MonsterTemplate.ETTIN, // uncommon (4 -> 0-3)
                MonsterTemplate.OOZE, // rare (2 -> 4-5)
                MonsterTemplate.BEHOLDER // very rare (1 -> 6)
        );

        assertThat(monsterService.createMonster(monsterTemplates).getName()).isEqualTo("Ettin");
        assertThat(monsterService.createMonster(monsterTemplates).getName()).isEqualTo("Ettin");
        assertThat(monsterService.createMonster(monsterTemplates).getName()).isEqualTo("Ooze");
        assertThat(monsterService.createMonster(monsterTemplates).getName()).isEqualTo("Ooze");
        assertThat(monsterService.createMonster(monsterTemplates).getName()).isEqualTo("Beholder");
    }
}