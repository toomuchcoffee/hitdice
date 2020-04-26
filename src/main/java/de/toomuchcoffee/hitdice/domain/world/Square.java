package de.toomuchcoffee.hitdice.domain.world;

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

    private Position getNW() {
        return Position.of(xMin, yMin);
    }

    private Position getSW() {
        return Position.of(xMax, yMin);
    }

    private Position getNE() {
        return Position.of(xMin, yMax);
    }

    private Position getSE() {
        return Position.of(xMax, yMax);
    }

    public Position edgeCenter(Direction orientation) {
        switch (orientation) {
            case NORTH:
                return getNW().interpolate(getNE());
            case EAST:
                return getNE().interpolate(getSE());
            case SOUTH:
                return getSW().interpolate(getSE());
            case WEST:
                return getNW().interpolate(getSW());
            default:
                throw new IllegalStateException("Unexpected value: " + orientation);
        }
    }

}
