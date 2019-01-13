package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    MONSTER("pastafarianism"),
    POTION("flask"),
    TREASURE("ring"),
    MAGIC_DOOR("dungeon");

    private final String symbol;

}
