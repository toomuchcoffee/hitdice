package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.event.factory.*;
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

    public Optional<Object> createEvent(List<MonsterFactory> monsterFactories) {
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

    private static final List<EventFactory<?>> FACTORIES = new ArrayList<>();
    static {
        FACTORIES.addAll(asList(PotionFactory.values()));
        FACTORIES.addAll(asList(ArmorFactory.values()));
        FACTORIES.addAll(asList(ShieldFactory.values()));
        FACTORIES.addAll(asList(WeaponFactory.values()));
    }

    @VisibleForTesting
    Object createItem() {
        return create(FACTORIES);
    }

    @VisibleForTesting
    Object createMonster(List<MonsterFactory> factories) {
        return create(factories);
    }

    private Object create(List<? extends EventFactory<?>> factories) {
        int sum = factories.stream().mapToInt(t -> t.getFrequency().getProbability()).sum();
        int roll = random.nextInt(sum);
        int p = 0;
        for (EventFactory<?> factory : factories) {
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