package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.Frequency;

public interface Item extends Event {
    String getName();
    boolean isMetallic();
    String getDisplayName();
    Frequency getFrequency();
}
