package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D8;

public class Dragon extends Monster {
    public Dragon() {
        super("Dragon",
                8,
                0,
                new Monster.NaturalWeapon("claws", 1, D8, 0),
                5,
                400,
                (attacker, defender) -> {
                    if (D20.check(5)) {
                        int damage = D8.roll(2);
                        defender.reduceHealth(damage);
                        return Optional.of(String.format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage));
                    }
                    return Optional.empty();
                });
    }
}
