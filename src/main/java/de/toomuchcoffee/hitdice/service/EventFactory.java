package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.event.Event;
import de.toomuchcoffee.hitdice.domain.event.Frequency;

public interface EventFactory<T> {
    Frequency getFrequency();
    Event<T> createEvent();
    T createObject();
    String name();
}
