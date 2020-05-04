package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode // FIXME remove
public class HandWeapon implements Item, Weapon {
    public static final HandWeapon FISTS = new HandWeapon(null, "fists", false, 0, D2::roll);

    private final EventFactory<HandWeapon> factory;
    private final String displayName;
    private final boolean metallic;
    private final int ordinal;

    private final Supplier<Integer> damage;
}
