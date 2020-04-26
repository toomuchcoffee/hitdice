package de.toomuchcoffee.hitdice.service.dungeonmaker;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class Point {
    private final int x, y;

    public Point interpolate(Point point) {
        int x = Math.min(this.getX(), point.getX()) + Math.abs(this.getX() - point.getX()) / 2;
        int y = Math.min(this.getY(), point.getY()) + Math.abs(this.getY() - point.getY()) / 2;
        return Point.of(x, y);
    }

    public Point add(Orientation orientation, int length) {
        switch (orientation) {
            case NORTH:
                return Point.of(x, y + length);
            case SOUTH:
                return Point.of(x, y - length);
            case EAST:
                return Point.of(x + length, y);
            case WEST:
                return Point.of(x -length, y);
            default:
                throw new IllegalStateException("Unexpected argument: " + orientation);
        }
    }
}
