package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.event.Event;
import de.toomuchcoffee.hitdice.domain.event.Frequency;

public interface EventFactory {
    Frequency getFrequency();
    Event createEvent();
    Object createObject(); // FIXME make type safe
    String name();
}
