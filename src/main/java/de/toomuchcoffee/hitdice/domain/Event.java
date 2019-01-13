package de.toomuchcoffee.hitdice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.EventType.MAGIC_DOOR;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Event {
    public static final Event MAGIC_DOOR_EVENT = new Event(MAGIC_DOOR);

    private final EventType type;
    private Object object;
}
