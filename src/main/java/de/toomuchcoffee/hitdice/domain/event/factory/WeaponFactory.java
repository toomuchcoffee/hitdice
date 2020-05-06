package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.*;

@Getter
@RequiredArgsConstructor
public enum WeaponFactory implements EventFactory<HandWeapon> {
    DAGGER("dagger", D4, true, COMMON),
    STAFF("staff", D4, false, COMMON),
    SHORTSWORD("shortsword", D6, true, UNCOMMON),
    MACE("mace", D6, true, UNCOMMON),
    LONGSWORD("longsword", D8, true, RARE),
    BATTLEAXE("battleaxe", D8, true, RARE),
    WARHAMMER("warhammer", D8, true, RARE),
    CLAYMORE("claymore", D10, true, VERY_RARE),
    MAUL("maul", D10, true, VERY_RARE);

    private final String displayName;
    private final Dice damage;
    private final boolean metallic;
    private final Frequency frequency;

    @Override
    public HandWeapon create() {
        return HandWeapon.builder()
                .displayName(displayName)
                .metallic(metallic)
                .ordinal(ordinal())
                .damage(damage)
                .build();
    }
}
