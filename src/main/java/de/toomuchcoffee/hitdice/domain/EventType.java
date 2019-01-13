package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    MONSTER("pastafarianism"),
    POTION("flask"),
    TREASURE("ring"),
    EXPLORED(null),
    MAGIC_DOOR("dungeon");

    private final String symbol;

}
