package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.service.EventFactory;

public interface Item {
    boolean isMetallic();
    String getDisplayName();
    int getOrdinal();
    EventFactory<?> getFactory(); // FIXME should not be dependent on event classes
}
