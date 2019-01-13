package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Potion {
    private final int power;
    private final Type type;

    public enum Type {
        HEALING, STRENGTH
    }
}
