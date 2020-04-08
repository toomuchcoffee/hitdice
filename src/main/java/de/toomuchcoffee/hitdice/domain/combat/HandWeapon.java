package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.item.Treasure;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;

@Getter
@RequiredArgsConstructor
public enum HandWeapon implements Treasure, Weapon {
    FISTS("fists", D2::roll, false),
    CLUB("club", D3::roll, false),
    STAFF("staff", D4::roll, false),
    DAGGER("dagger", D4::roll, true),
    SHORTSWORD("shortsword", D6::roll, true),
    MACE("mace", () -> D6.roll() + 1, true),
    LONGSWORD("longsword", D8::roll, true),
    CLAYMORE("claymore", D10::roll, true);

    private final String name;
    private final Supplier<Integer> damage;
    private final boolean metallic;

    private final EventType eventType = TREASURE;

    public String getDisplayName() {
        return name;
    }
}
