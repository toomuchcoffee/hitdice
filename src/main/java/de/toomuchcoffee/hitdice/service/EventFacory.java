package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.Frequency;

public interface EventFacory {
    Frequency getFrequency();
    Event create();
}
