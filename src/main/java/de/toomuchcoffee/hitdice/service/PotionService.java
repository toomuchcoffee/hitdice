package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.item.Potion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PotionService {
    private final Random random;

    public Potion createPotion() {
        List<Potion> potions = Arrays.stream(Potion.values()).filter(w -> w.getFrequency() != null).collect(toList());
        int sum = potions.stream().mapToInt(t -> t.getFrequency().getProbability()).sum();
        int roll = random.nextInt(sum);
        int p = 0;
        for (Potion potion : potions) {
            p += potion.getFrequency().getProbability();
            if (roll < p) {
                return potion;
            }
        }
        throw new IllegalStateException();
    }
}