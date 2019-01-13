package de.toomuchcoffee.hitdice.domain;

import static de.toomuchcoffee.hitdice.domain.EventType.MAGIC_DOOR;

public interface Event {
    EventType getEventType();

    Event MAGIC_DOOR_EVENT = () -> MAGIC_DOOR;
}
