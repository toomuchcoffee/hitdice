package de.toomuchcoffee.hitdice.service;


import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.event.factory.MonsterFactory;
import de.toomuchcoffee.hitdice.domain.event.factory.PotionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private Random random;

    private EventService eventService;

    @BeforeEach
    void setUp() throws Exception {
        eventService = new EventService(random);
    }

    @Test
    void createsTheRightMonster() {
        when(random.nextInt(7)).thenReturn(0, 3, 4, 5, 6);

        List<MonsterFactory> monsterFactories = List.of(
                MonsterFactory.ETTIN, // uncommon (4 -> 0-3)
                MonsterFactory.OOZE, // rare (2 -> 4-5)
                MonsterFactory.BEHOLDER // very rare (1 -> 6)
        );

        assertThat(((Monster) eventService.create(monsterFactories)).getName()).isEqualTo("Ettin");
        assertThat(((Monster) eventService.create(monsterFactories)).getName()).isEqualTo("Ettin");
        assertThat(((Monster) eventService.create(monsterFactories)).getName()).isEqualTo("Ooze");
        assertThat(((Monster) eventService.create(monsterFactories)).getName()).isEqualTo("Ooze");
        assertThat(((Monster) eventService.create(monsterFactories)).getName()).isEqualTo("Beholder");
    }

    @Test
    void createsTheRightItem() {
        when(random.nextInt(anyInt())).thenReturn(0);

        List<EventFactory<Potion>> factories = Arrays.asList(PotionFactory.values());

        assertThat(((Item) eventService.create(factories)).getDisplayName()).isEqualTo("health potion");
    }
}