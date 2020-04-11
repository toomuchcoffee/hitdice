package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.*;
import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public enum Armor implements Treasure {
    LEATHER("leather armor", 2, false, COMMON),
    CHAIN("chain mail", 3, true, RARE),
    PLATE("plate armor", 4, true, VERY_RARE);

    private final String name;
    private final int protection;
    private final boolean metallic;
    private final Frequency frequency;
    private final EventType eventType = TREASURE;

    public String getDisplayName() {
        return format("%s (ac: %d)", name, protection);
    }
}
