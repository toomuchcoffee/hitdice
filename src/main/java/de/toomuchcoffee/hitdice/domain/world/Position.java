package de.toomuchcoffee.hitdice.domain.world;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor(staticName = "of")
public class Position {
    private final int x;
    private final int y;

    public Position move(Direction direction) {
        switch (direction) {
            case NORTH:
                return Position.of(x - 1, y);
            case EAST:
                return Position.of(x, y + 1);
            case SOUTH:
                return Position.of(x + 1, y);
            case WEST:
                return Position.of(x, y - 1);
            default:
                throw new IllegalStateException("Unknown value: " + direction);
        }
    }
}
