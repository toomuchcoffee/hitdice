package de.toomuchcoffee.hitdice.domain.world;

import static de.toomuchcoffee.hitdice.domain.world.EventType.MAGIC_DOOR;

public interface Event {
    EventType getEventType();

    Event MAGIC_DOOR_EVENT = () -> MAGIC_DOOR;
}
