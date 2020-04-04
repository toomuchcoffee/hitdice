package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D4;

public class Vampire extends Monster {
    public Vampire() {
        super("Vampire",
                5,
                2,
                new Monster.NaturalWeapon("bite", 2, D4, 0),
                0,
                200,
                (attacker, defender) -> {
                    if (D20.check(5)) {
                        if (defender instanceof Hero) {
                            Hero hero = (Hero) defender;
                            hero.getStrength().decrease();
                            return Optional.of("Don't you just hate vampires? This fella just sucked away one point of strength from you!");
                        }
                    }
                    return Optional.empty();
                });
    }
}
