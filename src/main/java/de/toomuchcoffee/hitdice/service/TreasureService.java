package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.combat.HandWeapon;
import de.toomuchcoffee.hitdice.domain.item.Armor;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TreasureService {
    private final Random random;

    public Treasure createTreasure() {
        List<Treasure> treasures = new ArrayList<>();
        treasures.addAll(Arrays.stream(HandWeapon.values()).filter(w -> w.getFrequency() != null).collect(toList()));
        treasures.addAll(asList(Armor.values()));
        int sum = treasures.stream().mapToInt(t -> t.getFrequency().getProbability()).sum();
        int roll = random.nextInt(sum);
        int p = 0;
        for (Treasure treasure : treasures) {
            p += treasure.getFrequency().getProbability();
            if (roll < p) {
                return treasure;
            }
        }
        throw new IllegalStateException();
    }
}