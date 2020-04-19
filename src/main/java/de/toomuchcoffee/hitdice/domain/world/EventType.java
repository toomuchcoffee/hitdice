package de.toomuchcoffee.hitdice.domain.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    MONSTER("pastafarianism"),
    POTION("flask"),
    TREASURE("coins"),
    MAGIC_DOOR("dungeon");

    private final String symbol;

}
