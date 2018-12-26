package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    EMPTY(" ", false),
    MONSTER("¥", true),
    POTION("†", true),
    TREASURE("$", true),
    EXPLORED(" ", false),
    MAGIC_DOOR("§", true);

    private final String symbol;
    private final boolean occupied;

}
