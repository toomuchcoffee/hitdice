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

    public Position shift(Direction direction) {
        return shift(direction, 1);
    }

    public Position shift(Direction orientation, int distance) {
        switch (orientation) {
            case NORTH:
                return Position.of(x - distance, y);
            case EAST:
                return Position.of(x, y + distance);
            case SOUTH:
                return Position.of(x + distance, y);
            case WEST:
                return Position.of(x, y - distance);
            default:
                throw new IllegalStateException("Unexpected argument: " + orientation);
        }
    }

    public Position interpolate(Position point) {
        int x = Math.min(this.getX(), point.getX()) + Math.abs(this.getX() - point.getX()) / 2;
        int y = Math.min(this.getY(), point.getY()) + Math.abs(this.getY() - point.getY()) / 2;
        return Position.of(x, y);
    }
}
