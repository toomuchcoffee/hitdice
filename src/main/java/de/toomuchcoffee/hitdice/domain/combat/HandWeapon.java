package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.item.Treasure;
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
public enum HandWeapon implements Treasure, Weapon {
    FISTS("fists", D2::roll, false, null),
    CLUB("club", D3::roll, false, COMMON),
    STAFF("staff", D4::roll, false, COMMON),
    DAGGER("dagger", D4::roll, true, COMMON),
    SHORTSWORD("shortsword", D6::roll, true, COMMON),
    MACE("mace", D6::roll, true, RARE),
    LONGSWORD("longsword", D8::roll, true, RARE),
    WARHAMMER("warhammer", D8::roll, true, RARE),
    CLAYMORE("claymore", D10::roll, true, VERY_RARE);

    private final String name;
    private final Supplier<Integer> damage;
    private final boolean metallic;
    private final Frequency frequency;

    private final EventType eventType = TREASURE;

    public String getDisplayName() {
        return name;
    }
}
