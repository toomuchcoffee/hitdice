package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;
import static java.lang.Math.abs;
import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public enum HandWeapon implements Treasure, Weapon {
    FISTS("fists", 1, D2, 0, false),
    CLUB("club", 1, D3, 0, false),
    STAFF("staff", 1, D4, 1, false),
    DAGGER("dagger", 1, D4, 0, true),
    SHORTSWORD("shortsword", 1, D6, 0, true),
    MACE("mace", 1, D6, 1, true),
    LONGSWORD("longsword", 1, D8, 0, true),
    CLAYMORE("claymore", 1, D10, 0, true);

    private final String name;
    private final int diceNumber;
    private final Dice dice;
    private final int bonus;
    private final boolean metallic;
    private final EventType eventType = TREASURE;

    public String getDisplayName() {
        if (bonus < 0) {
            return format("%s (dmg: %dD%s - %d)", name, diceNumber, dice.sides, abs(bonus));
        } else if (bonus > 0) {
            return format("%s (dmg: %dD%s + %d)", name, diceNumber, dice.sides, bonus);
        }
        return format("%s (dmg: %dD%s)", name, diceNumber, dice.sides);
    }
}
