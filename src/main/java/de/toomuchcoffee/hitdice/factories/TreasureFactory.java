package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.Treasure;
import de.toomuchcoffee.hitdice.domain.Weapon;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.Armor.*;
import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.Weapon.*;
import static java.lang.String.format;

public class TreasureFactory {
    public static Treasure createTreasure() {
        int result = D100.roll();
        if (result < 12) {
            return DAGGER;
        } else if (result < 24) {
            return CLUB;
        } else if (result < 36) {
            return STAFF;
        } else if (result < 48) {
            return LEATHER;
        } else if (result < 60) {
            return SHORTSWORD;
        } else if (result < 70) {
            return MACE;
        } else if (result < 80) {
            return CHAIN;
        } else if (result < 88) {
            return LONGSWORD;
        } else if (result < 96) {
            return PLATE;
        } else {
            return new Weapon("magic sword", 1, Dice.D8, 1, true) {
                @Override
                public Optional<String> specialDamage(Combatant defender) {
                    if (D20.roll() < 6) {
                        int extraDamage = D4.roll();
                        defender.decreaseCurrentStaminaBy(extraDamage);
                        return Optional.of(format("Wohoo, the magic sword lit up like a torch: The fire caused %d extra points of damage on your enemy.", extraDamage));
                    }
                    return Optional.empty();
                }
            };
        }
    }
}
