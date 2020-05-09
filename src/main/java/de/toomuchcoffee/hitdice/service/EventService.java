package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.domain.event.factory.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.forLevel;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.values;
import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class EventService {
    private final Random random;

    public Optional<Object> createEvent(int heroLevel) {
        switch (D20.roll()) {
            case 1:
                List<EventFactory<?>> potionFactories = init(asList(PotionFactory.values()), newHashSet(values()));
                return Optional.of(create(potionFactories));
            case 2:
                List<EventFactory<?>> monsterFactories = init(asList(MonsterFactory.values()), newHashSet(forLevel(heroLevel)));
                return Optional.of(create(monsterFactories));
            case 3:
                List<EventFactory<?>> itemFactories = init(
                        newArrayList(asList(ArmorFactory.values()), asList(ShieldFactory.values()), asList(WeaponFactory.values())).stream()
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()),
                        newHashSet(forLevel(heroLevel, 1, -1)));
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

    @VisibleForTesting
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