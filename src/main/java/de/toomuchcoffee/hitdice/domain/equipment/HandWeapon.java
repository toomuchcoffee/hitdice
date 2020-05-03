package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.*;

@Getter
@RequiredArgsConstructor
public enum HandWeapon implements Item, Weapon {
    FISTS("fists", D2::roll, false, null),
    DAGGER("dagger", D4::roll, true, COMMON),
    STAFF("staff", D4::roll, false, COMMON),
    SHORTSWORD("shortsword", D6::roll, true, UNCOMMON),
    MACE("mace", D6::roll, true, UNCOMMON),
    LONGSWORD("longsword", D8::roll, true, RARE),
    BATTLEAXE("battleaxe", D8::roll, true, RARE),
    WARHAMMER("warhammer", D8::roll, true, RARE),
    CLAYMORE("claymore", D10::roll, true, VERY_RARE),
    MAUL("maul", D10::roll, true, VERY_RARE);

    private final String displayName;
    private final Supplier<Integer> damage;
    private final boolean metallic;
    private final Frequency frequency;

    private final EventType eventType = TREASURE;

    @Override
    public String getName() {
        return name();
    }
}
