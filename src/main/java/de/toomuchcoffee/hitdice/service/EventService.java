package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.domain.event.factory.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.forLevel;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.values;

@Service
@RequiredArgsConstructor
public class EventService {
    private final Random random;

    public Optional<Object> createEvent(int heroLevel) {
        switch (D20.roll()) {
            case 1:
                List<EventFactory<?>> potionFactories = init(List.of(PotionFactory.values()), Set.of(values()));
                return Optional.of(create(potionFactories));
            case 2:
                List<EventFactory<?>> monsterFactories = init(List.of(MonsterFactory.values()), new HashSet<>(forLevel(heroLevel)));
                return Optional.of(create(monsterFactories));
            case 3:
                List<EventFactory<?>> itemFactories = init(
                        Stream.of(List.of(ArmorFactory.values()), List.of(ShieldFactory.values()), List.of(WeaponFactory.values()))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()),
                        new HashSet<>(forLevel(heroLevel, 1, -1)));
                return Optional.of(create(itemFactories));
            default:
                return Optional.empty();
        }
    }

    private List<EventFactory<?>> init(List<EventFactory<?>> factories, Set<Frequency> frequencies){
        return factories.stream()
                .filter(f -> frequencies.contains(f.getFrequency()))
                .collect(Collectors.toList());
    }

    Object create(List<? extends EventFactory<?>> factories) {
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
}