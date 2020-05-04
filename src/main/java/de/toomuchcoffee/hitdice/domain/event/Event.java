package de.toomuchcoffee.hitdice.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Event<T> {
    private final EventType type;
    private final T object;
}
