package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.combat.HandWeapon;
import de.toomuchcoffee.hitdice.domain.item.Armor;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
import de.toomuchcoffee.hitdice.domain.world.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Dice.D100;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EventService {
    private final Random random;

    public Event createEvent(List<MonsterTemplate> monsterTemplates) {
        switch (D100.roll()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return createPotion();
            case 6:
            case 7:
                return createArmor();
            case 8:
            case 9:
            case 10:
                return createWeapon();
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return createMonster(monsterTemplates);
            default:
                return null;
        }
    }

    private Potion createPotion() {
        List<Potion> potions = asList(Potion.values());
        return create(potions);
    }

    private Treasure createArmor() {
        List<Armor> armors = asList(Armor.values());
        return create(armors);
    }

    @VisibleForTesting
    HandWeapon createWeapon() {
        List<HandWeapon> weapons = Arrays.stream(HandWeapon.values())
                .filter(w -> w.getFrequency() != null)
                .collect(toList());
        return create(weapons);
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