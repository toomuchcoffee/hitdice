package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

import static de.toomuchcoffee.hitdice.service.DiceService.Dice.D6;

@Service
@RequiredArgsConstructor
public class HeroService {
    private final DiceService diceService;

    public Hero create() {
        int strength = diceService.roll(D6, 3);
        int dexterity = diceService.roll(D6, 3);
        int stamina = diceService.roll(D6, 3);
        return new Hero(strength, dexterity, stamina, stamina);
    }

    public int experienceNeededForLevel(int level) {
        return IntStream.range(0, level).map(l -> (l + 1) * 100).sum();
    }
}
