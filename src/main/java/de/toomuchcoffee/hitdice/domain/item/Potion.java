package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeName;
import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.world.EventType.POTION;

@RequiredArgsConstructor
@Getter
public class Potion implements Event {
    private final int power;
    private final AttributeName type;
    private final EventType eventType = POTION;
}
