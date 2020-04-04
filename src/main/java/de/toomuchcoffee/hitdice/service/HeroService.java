package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

import static de.toomuchcoffee.hitdice.service.Dice.D6;
import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class HeroService {

    public Hero create() {
        int strength = D6.roll(3);
        int dexterity = D6.roll(3);
        int stamina = D6.roll(3);
        return new Hero(strength, dexterity, stamina, stamina);
    }

    public void collectTreasure(Hero hero, Treasure treasure) {
        if (treasure instanceof Armor) {
            hero.setArmor((Armor) treasure);
        } else if (treasure instanceof HandWeapon) {
            hero.setWeapon((HandWeapon) treasure);
        }
    }

    public void drinkPotion(Hero hero, Potion potion) {
        hero.drink(potion);
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
