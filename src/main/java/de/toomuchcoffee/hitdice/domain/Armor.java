package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.EventType.TREASURE;
import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public enum Armor implements Treasure {
    LEATHER("leather armor", 2, false),
    CHAIN("chain mail", 3, true),
    PLATE("plate armor", 4, true);

    private final String name;
    private final int protection;
    private final boolean metallic;

    public String getDisplayName() {
        return format("%s (ac: %d)", name, protection);
    }

    @Override
    public EventType getEventType() {
        return TREASURE;
    }
}
