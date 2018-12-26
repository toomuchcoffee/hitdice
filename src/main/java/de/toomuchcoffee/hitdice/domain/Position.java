package de.toomuchcoffee.hitdice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
public class Position {
    private final int x;
    private final int y;
}
