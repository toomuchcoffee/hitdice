package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (hero.getExperience() >= experienceNeededForNextLevel(hero)) {
            hero.setLevel(hero.getLevel() + 1);
            // Main.draw("Yohoo, you gained a new experience level!" + lineSeparator() +
            //       "You can add one point to one of your attribute scores." + lineSeparator() +
            //     "Strength and dexterity can be only increased to a maximum of 18.");
            increaseAttribute(hero);
        }
    }

    private void increaseAttribute(Hero hero) {
        boolean valid = false;
        while (!valid) {
            //String opt = Main.dialog("Choose the attribute you want to increase: strength (s), dexterity (d), stamina (a): ");

        }

    }

    private int experienceNeededForNextLevel(Hero hero) {
        int xp = 0;
        for (int i=0; i<=hero.getLevel(); i++) {
            xp += i * 100;
        }
        return xp;
    }
}
