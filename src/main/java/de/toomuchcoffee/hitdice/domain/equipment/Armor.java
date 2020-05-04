package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.*;

@Getter
@RequiredArgsConstructor
public enum Armor implements Item {
    PADDED("padded armor", 1, false, COMMON),
    LEATHER("leather armor", 2, false, UNCOMMON),
    CHAIN("chain mail", 3, true, RARE),
    PLATE("plate armor", 4, true, VERY_RARE);

    private final String displayName;
    private final int protection;
    private final boolean metallic;
    private final Frequency frequency;
    private final EventType eventType = TREASURE;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public Event create() {
        return new Event(TREASURE, this);
    }
}
