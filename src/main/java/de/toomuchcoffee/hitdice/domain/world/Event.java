package de.toomuchcoffee.hitdice.domain.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Event {
    private final EventType type;
    private final Object object;
}
