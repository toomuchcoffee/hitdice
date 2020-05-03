package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.equipment.*;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
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

    public Optional<Event> createEvent(List<MonsterTemplate> monsterTemplates) {
        switch (D20.roll()) {
            case 1:
            case 2:
                return Optional.of(createItem());
            case 3:
                return Optional.of(createMonster(monsterTemplates));
            default:
                return Optional.empty();
        }
    }

    private static final List<Item> AVAILABLE_ITEMS = new ArrayList<>();
    static {
        AVAILABLE_ITEMS.addAll(asList(Potion.values()));
        AVAILABLE_ITEMS.addAll(asList(Armor.values()));
        AVAILABLE_ITEMS.addAll(asList(Shield.values()));
        AVAILABLE_ITEMS.addAll(Arrays.stream(HandWeapon.values())
                .filter(w -> w.getFrequency() != null)
                .collect(toList()));
    }

    @VisibleForTesting
    Item createItem() {
        return create(AVAILABLE_ITEMS);
    }

    @VisibleForTesting
    Monster createMonster(List<MonsterTemplate> templates) {
        return create(templates);
    }

    private <T> T create(List<? extends EventTemplate<T>> templates) {
        int sum = templates.stream().mapToInt(t -> t.getFrequency().getProbability()).sum();
        int roll = random.nextInt(sum);
        int p = 0;
        for (EventTemplate<T> template : templates) {
            p += template.getFrequency().getProbability();
            if (roll < p) {
                return template.create();
            }
        }
        throw new IllegalStateException();
    }

    public List<MonsterTemplate> findTemplates(int heroLevel) {
        return Arrays.stream(MonsterTemplate.values())
                .filter(t -> t.getLevel() > heroLevel - 5 && t.getLevel() <= heroLevel)
                .collect(toList());
    }
}