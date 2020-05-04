package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.service.EventFactory;

public interface Item {
    boolean isMetallic();
    String getDisplayName();
    EventFactory getFactory();
}
