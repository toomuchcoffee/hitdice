package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.*;

@Getter
@RequiredArgsConstructor
public enum WeaponFactory implements EventFactory<HandWeapon> {
    DAGGER("dagger", D4.roller(), true, COMMON),
    STAFF("staff", D4.roller(), false, COMMON),
    SHORTSWORD("shortsword", D6.roller(), true, UNCOMMON),
    MACE("mace", D6.roller(), true, UNCOMMON),
    LONGSWORD("longsword", D8.roller(), true, RARE),
    BATTLEAXE("battleaxe", D8.roller(), true, RARE),
    WARHAMMER("warhammer", D8.roller(), true, RARE),
    CLAYMORE("claymore", D10.roller(), true, VERY_RARE),
    MAUL("maul", D10.roller(), true, VERY_RARE);

    private final String displayName;
    private final Supplier<Integer> damage;
    private final boolean metallic;
    private final Frequency frequency;

    @Override
    public HandWeapon create() {
        return HandWeapon.builder()
                .factory(this)
                .displayName(displayName)
                .metallic(metallic)
                .ordinal(ordinal())
                .damage(damage)
                .build();
    }
}
