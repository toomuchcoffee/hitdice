package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.Frequency;

public interface Treasure extends Event {
    String getName();
    boolean isMetallic();
    String getDisplayName();
    Frequency getFrequency();
}
