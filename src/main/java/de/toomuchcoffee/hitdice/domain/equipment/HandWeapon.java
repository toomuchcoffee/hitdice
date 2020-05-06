package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;


@Getter
@SuperBuilder
public class HandWeapon extends Item implements Weapon {
    public static final HandWeapon FISTS = HandWeapon.builder()
            .displayName("fists")
            .metallic(false)
            .damage(D2)
            .build();

    private final Dice damage;
}
