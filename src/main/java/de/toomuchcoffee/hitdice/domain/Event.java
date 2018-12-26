package de.toomuchcoffee.hitdice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.EventType.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Event {
    public static final Event EMPTY_EVENT = new Event(EMPTY);
    public static final Event MAGIC_DOOR_EVENT = new Event(MAGIC_DOOR);
    public static final Event EXPLORED_EVENT = new Event(EXPLORED);

    private final EventType type;
    private Object object;
}
