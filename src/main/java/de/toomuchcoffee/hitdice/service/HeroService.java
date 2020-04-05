package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.item.Armor;
import de.toomuchcoffee.hitdice.domain.item.HandWeapon;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HeroService {

    public Hero create() {
        Hero hero = new Hero();
        hero.initialize();
        return hero;
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
