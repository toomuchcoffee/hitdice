package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HeroService {

    public Hero create() {
        Hero hero = new Hero();
        hero.initialize();
        return hero;
    }

    public void collectTreasure(Hero hero, Item item) {
        hero.addEquipment(item);
    }

    public void drinkPotion(Hero hero, AttributeType type) {
        Iterator<Item> it = hero.getEquipment().iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item instanceof Potion && ((Potion) item).getType().equals(type)) {
                it.remove();
                Potion potion = (Potion) item;
                hero.getAttributes().get(potion.getType()).increase(potion.getPotency().get());
                return;
            }
        }
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
