package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.event.Event;
import de.toomuchcoffee.hitdice.domain.event.EventType;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.event.EventType.TREASURE;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.*;

@Getter
@RequiredArgsConstructor
public enum WeaponFactory implements EventFactory {
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
    public Event createEvent() {
        return new Event(TREASURE, createObject());
    }

    @Override
    public Item createObject() {
        return new HandWeapon(this, displayName, metallic, damage);
    }
}
