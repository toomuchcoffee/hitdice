package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true) // FIXME remove
public class HandWeapon extends Item implements Weapon {
    public static final HandWeapon FISTS = HandWeapon.builder()
            .displayName("fists")
            .metallic(false)
            .damage(D2::roll)
            .build();

    private final Supplier<Integer> damage;
}
