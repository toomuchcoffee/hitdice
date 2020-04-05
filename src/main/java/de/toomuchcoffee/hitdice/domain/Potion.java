package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.EventType.POTION;

@RequiredArgsConstructor
@Getter
public class Potion implements Event {
    private final int power;
    private final AttributeName type;
    private final EventType eventType = POTION;
}
