package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.world.Event;

public interface Treasure extends Event {
    String getName();
    boolean isMetallic();
    String getDisplayName();
}
