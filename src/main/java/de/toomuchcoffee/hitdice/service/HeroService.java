package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

import static de.toomuchcoffee.hitdice.service.Dice.D6;
import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class HeroService {

    public Hero create() {
        int strength = 3 * D6.roll();
        int dexterity = 3 * D6.roll();
        int stamina = 3 * D6.roll();
        return new Hero(strength, dexterity, stamina, stamina);
    }


    public void increaseExperience(Hero hero, int gainedXp) {
        hero.setExperience(hero.getExperience() + gainedXp);

        while (hero.getExperience() > xpForNextLevel(hero.getLevel())) {
            hero.setLevel(hero.getLevel() + 1);
            int healthIncrease = max(1, D6.roll() + hero.getStamina().getBonus());
            hero.setMaxHealth(hero.getMaxHealth() + healthIncrease);
            hero.setHealth(hero.getHealth() + healthIncrease);
        }
    }

    @VisibleForTesting
    int xpForNextLevel(int level) {
        return IntStream.range(0, level).map(l -> (l + 1) * 100).sum();
    }
}
