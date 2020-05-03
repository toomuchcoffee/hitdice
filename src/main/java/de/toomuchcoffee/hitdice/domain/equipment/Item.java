package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import de.toomuchcoffee.hitdice.service.EventTemplate;

public interface Item extends Event, EventTemplate<Item> {
    String getName();
    boolean isMetallic();
    String getDisplayName();
    Frequency getFrequency();
    default Item create() {
        return this;
    }
}
