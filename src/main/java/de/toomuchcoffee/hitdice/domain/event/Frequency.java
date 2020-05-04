package de.toomuchcoffee.hitdice.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Frequency {
    VERY_RARE(1), RARE(2), UNCOMMON(4), COMMON(13);

    private final int probability;
}
