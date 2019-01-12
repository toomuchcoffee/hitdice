package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.service.DiceService.Dice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.service.DiceService.Dice.*;
import static java.lang.Math.abs;
import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public enum Weapon implements Treasure, Attack {
    FISTS("fists", 1, D2, 0, false),
    DAGGER("dagger", 1, D4, 0, true),
    SHORTSWORD("shortsword", 1, D6, 0, true),
    LONGSWORD("longsword", 1, D8, 0, true),
    CLAYMORE("claymore", 1, D10, 0, true),
    CLUB("club", 1, D4, 0, false),
    MACE("mace", 1, D6, 1, true),
    STAFF("staff", 1, D4, 1, false);

    private final String name;
    private final int diceNumber;
    private final Dice dice;
    private final int bonus;
    private final boolean metallic;

    public String getDisplayName() {
        if (bonus < 0) {
            return format("%s (dmg: %dD%s - %d)", name, diceNumber, dice.sides, abs(bonus));
        } else if (bonus > 0) {
            return format("%s (dmg: %dD%s + %d)", name, diceNumber, dice.sides, bonus);
        }
        return format("%s (dmg: %dD%s)", name, diceNumber, dice.sides);
    }
}
