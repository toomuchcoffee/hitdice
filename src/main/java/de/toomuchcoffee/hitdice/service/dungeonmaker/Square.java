package de.toomuchcoffee.hitdice.service.dungeonmaker;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Square {
    protected int xMin, yMin, xMax, yMax;

    boolean contains(int x, int y) {
        return x >= xMin && x <= xMax && y >= yMin && y <= yMax;
    }

    public boolean contains(Square square) {
        return contains(square.xMin, square.yMin) &&
                contains(square.xMin, square.yMax) &&
                contains(square.xMax, square.yMin) &&
                contains(square.xMax, square.yMax);
    }

    public boolean intersects(Square square) {
        return !outside(square);
    }

    private boolean outside(Square square) {
        return square.getXMin() > xMax ||
                square.getXMax() < xMin ||
                square.getYMin() > yMax ||
                square.getYMax() < yMin;
    }

    private Point getBL() {
        return Point.of(xMin, yMin);
    }

    private Point getBR() {
        return Point.of(xMax, yMin);
    }

    private Point getTL() {
        return Point.of(xMin, yMax);
    }

    private Point getTR() {
        return Point.of(xMax, yMax);
    }

    public Point edgeCenter(Orientation orientation) {
        switch (orientation) {
            case SOUTH:
                return getBL().interpolate(getBR());
            case NORTH:
                return getTL().interpolate(getTR());
            case EAST:
                return getBR().interpolate(getTR());
            case WEST:
                return getBL().interpolate(getTL());
            default:
                throw new IllegalStateException("Unexpected value: " + orientation);
        }
    }

}
