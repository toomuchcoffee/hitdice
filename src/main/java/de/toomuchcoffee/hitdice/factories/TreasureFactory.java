package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.Main;
import de.toomuchcoffee.hitdice.domain.Armor;
import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Treasure;
import de.toomuchcoffee.hitdice.domain.Weapon;
import de.toomuchcoffee.hitdice.utilities.Dice;

import static de.toomuchcoffee.hitdice.utilities.Dice.*;

public class TreasureFactory {
    public static Armor LEATHER = new Armor("leather armor", 2, false);
    public static Armor CHAIN = new Armor("chain mail", 3, true);
    public static Armor PLATE = new Armor("plate armor", 4, true);
    public static Weapon DAGGER = new Weapon("dagger", 1, D4, 0, true);
    public static Weapon SHORTSWORD = new Weapon("shortsword", 1, Dice.D6, 0, true);
    public static Weapon LONGSWORD = new Weapon("longsword", 1, Dice.D8, 0, true);
    public static Weapon CLUB = new Weapon("club", 1, D4, 0, false);
    public static Weapon MACE = new Weapon("mace", 1, Dice.D6, 1, true);
    public static Weapon STAFF = new Weapon("staff", 1, D4, 1, false);

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
                public void specialDamage(Combatant defender) {
                    if (D20.roll() < 6) {
                        int extraDamage = D4.roll();
                        defender.decreaseCurrentStaminaBy(extraDamage);
                        Main.draw("Wohoo, the magic sword lit up like a torch");
                        Main.draw("The fire caused %d extra points of damage on your enemy.", extraDamage);
                    }
                }
            };
        }
    }
}
