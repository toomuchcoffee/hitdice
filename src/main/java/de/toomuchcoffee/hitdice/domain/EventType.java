package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    EMPTY(null, false),
    MONSTER("pastafarianism", true),
    POTION("flask", true),
    TREASURE("ring", true),
    EXPLORED(null, false),
    MAGIC_DOOR("dungeon", true);

    private final String symbol;
    private final boolean occupied;

}
