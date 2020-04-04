package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HeroService {

    public Hero create() {
        return new Hero();
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
            hero.levelUp();
        }
    }

    @VisibleForTesting
    int xpForNextLevel(int level) {
        return IntStream.range(0, level).map(l -> (l + 1) * 100).sum();
    }
}
