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
        return new Hero(diceService.roll(D6, 3), diceService.roll(D6, 3), diceService.roll(D6, 3));
    }

    public void gainExperience(Hero hero, int experience) {
        hero.increaseExperience(experience);
        //Main.draw("You gained %d experience points.", experience);
        if (hero.getExperience() >= experienceNeededForLevel(hero.getLevel())) {
            hero.setLevel(hero.getLevel() + 1);
            // Main.draw("Yohoo, you gained a new experience level!" + lineSeparator() +
            //       "You can add one point to one of your attribute scores." + lineSeparator() +
            //     "Strength and dexterity can be only increased to a maximum of 18.");
            //increaseAttribute(hero);
        }
    }

    public int experienceNeededForLevel(int level) {
        return IntStream.range(0, level).map(l -> (l + 1) * 100).sum();
    }
}
