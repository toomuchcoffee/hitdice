package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.equipment.Shield;
import de.toomuchcoffee.hitdice.domain.event.Event;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.event.Frequency.RARE;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.UNCOMMON;

@Getter
@RequiredArgsConstructor
public enum ShieldFactory implements EventFactory<Shield> {
    SMALL("small shield", 2, false, UNCOMMON),
    LARGE("large shield", 3, true, RARE);

    private final String displayName;
    private final int defense;
    private final boolean metallic;
    private final Frequency frequency;

    @Override
    public Event<Shield> createEvent() {
        return new Event<>(createObject());
    }

    @Override
    public Shield createObject() {
        return new Shield(this, displayName, metallic, defense);
    }
}
