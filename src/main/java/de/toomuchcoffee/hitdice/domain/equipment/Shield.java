package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.world.EventType.TREASURE;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.RARE;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.UNCOMMON;

@Getter
@RequiredArgsConstructor
public enum Shield implements Item {
    SMALL("small shield", 2, false, UNCOMMON),
    LARGE("large shield", 3, true, RARE);

    private final String displayName;
    private final int defense;
    private final boolean metallic;
    private final Frequency frequency;
    private final EventType eventType = TREASURE;

    @Override
    public String getName() {
        return name();
    }
}
