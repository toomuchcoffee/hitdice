package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D4;

public class Ghoul extends Monster {
    public Ghoul() {
        super("Ghoul",
                2,
                -1,
                new Monster.NaturalWeapon("claws", 1, D4, 0),
                0,
                40,
                (attacker3, defender3) -> {
                    if (D20.check(5)) {
                        if (defender3 instanceof Hero) {
                            Hero hero2 = (Hero) defender3;
                            hero2.getStamina().decrease();
                            return Optional.of("Oh my, the foulness of the Ghoul has drained your stamina by one point!");
                        }
                    }
                    return Optional.empty();
                });
    }
}
