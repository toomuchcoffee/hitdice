package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.equipment.Armor;
import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.equipment.Shield;
import de.toomuchcoffee.hitdice.domain.monster.MonsterFactory;
import de.toomuchcoffee.hitdice.domain.world.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EventService {
    private final Random random;

    public Optional<Event> createEvent(List<MonsterFactory> monsterFactories) {
        switch (D20.roll()) {
            case 1:
            case 2:
                return Optional.of(createItem());
            case 3:
                return Optional.of(createMonster(monsterFactories));
            default:
                return Optional.empty();
        }
    }

    private static final List<EventFacory> FACTORIES = new ArrayList<>();
    static {
        FACTORIES.addAll(asList(Potion.values()));
        FACTORIES.addAll(asList(Armor.values()));
        FACTORIES.addAll(asList(Shield.values()));
        FACTORIES.addAll(Arrays.stream(HandWeapon.values())
                .filter(w -> w.getFrequency() != null)
                .collect(toList()));
    }

    @VisibleForTesting
    Event createItem() {
        return create(FACTORIES);
    }

    @VisibleForTesting
    Event createMonster(List<MonsterFactory> templates) {
        return create(templates);
    }

    private Event create(List<? extends EventFacory> factories) {
        int sum = factories.stream().mapToInt(t -> t.getFrequency().getProbability()).sum();
        int roll = random.nextInt(sum);
        int p = 0;
        for (EventFacory factory : factories) {
            p += factory.getFrequency().getProbability();
            if (roll < p) {
                return factory.create();
            }
        }
        throw new IllegalStateException();
    }

    public List<MonsterFactory> findFactories(int heroLevel) {
        return Arrays.stream(MonsterFactory.values())
                .filter(t -> t.getLevel() > heroLevel - 5 && t.getLevel() <= heroLevel)
                .collect(toList());
    }
}