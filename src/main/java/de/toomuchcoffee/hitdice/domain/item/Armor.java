package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.world.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;
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
    private final EventType eventType = TREASURE;

    public String getDisplayName() {
        return format("%s (ac: %d)", name, protection);
    }
}