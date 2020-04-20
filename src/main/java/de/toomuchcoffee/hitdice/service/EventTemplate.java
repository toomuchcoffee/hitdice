package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.world.Frequency;

public interface EventTemplate<T> {
    Frequency getFrequency();
    T create();
}
