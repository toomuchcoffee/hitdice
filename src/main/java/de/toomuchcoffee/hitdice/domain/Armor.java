package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Armor implements Treasure {
    private final String name;
    private final int protection;
    private final boolean metallic;
}
