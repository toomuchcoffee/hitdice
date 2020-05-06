package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.Dice;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;


@Getter
@SuperBuilder
public class Weapon extends Item {
    public static final Weapon FISTS = Weapon.builder()
            .displayName("fists")
            .metallic(false)
            .damage(D2)
            .build();

    private final Dice damage;

    public static Weapon simple(String displayName, Dice damage) {
        return Weapon.builder().displayName(displayName).damage(damage).build();
    }
}
