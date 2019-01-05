package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public class Weapon implements Treasure {
    public static final Weapon FISTS = new Weapon("fists", 1, D2, 0, true);
    public static final Weapon DAGGER = new Weapon("dagger", 1, D4, 0, true);
    public static final Weapon SHORTSWORD = new Weapon("shortsword", 1, D6, 0, true);
    public static final Weapon LONGSWORD = new Weapon("longsword", 1, Dice.D8, 0, true);
    public static final Weapon CLUB = new Weapon("club", 1, D4, 0, false);
    public static final Weapon MACE = new Weapon("mace", 1, D6, 1, true);
    public static final Weapon STAFF = new Weapon("staff", 1, D4, 1, false);

    private final String name;
    private final int diceNumber;
    private final Dice dice;
    private final int bonus;
    private final boolean metallic;

    public String getDisplayName() {
        if (bonus < 0) {
            return format("%s (dmg: %dD%s - %d)", name, diceNumber, dice.sides, -1 * bonus);
        } else if (bonus > 0) {
            return format("%s (dmg: %dD%s + %d)", name, diceNumber, dice.sides, bonus);
        }
        return format("%s (dmg: %dD%s)", name, diceNumber, dice.sides);
    }

    public int damage() {
        return dice.roll(diceNumber) + bonus;
    };

    public Optional<String> specialDamage(Combatant defender) {
        return Optional.empty();
    }
}
