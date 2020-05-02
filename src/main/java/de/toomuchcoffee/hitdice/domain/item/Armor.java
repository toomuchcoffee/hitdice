package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import de.toomuchcoffee.hitdice.service.EventTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.*;

@Getter
@RequiredArgsConstructor
public enum Armor implements Treasure, EventTemplate<Treasure> {
    LEATHER("leather armor", 2, false, COMMON),
    CHAIN("chain mail", 3, true, RARE),
    PLATE("plate armor", 4, true, VERY_RARE);

    private final String displayName;
    private final int protection;
    private final boolean metallic;
    private final Frequency frequency;
    private final EventType eventType = TREASURE;

    @Override
    public Treasure create() {
        return this;
    }

    @Override
    public String getName() {
        return name();
    }
}
