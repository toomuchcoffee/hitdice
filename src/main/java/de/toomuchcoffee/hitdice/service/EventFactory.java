package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.event.Frequency;

public interface EventFactory<T> {
    Frequency getFrequency();
    T create();
    String name();
}
