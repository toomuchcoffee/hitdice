package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import de.toomuchcoffee.hitdice.service.EventFacory;

public interface Item extends EventFacory {
    String getName();
    boolean isMetallic();
    String getDisplayName();
    Frequency getFrequency();
    Event create();
}
