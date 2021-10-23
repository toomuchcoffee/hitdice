package de.toomuchcoffee.hitdice.domain.world;

import lombok.Getter;

import java.util.List;

import static de.toomuchcoffee.hitdice.domain.world.Direction.*;

@Getter
public class Hallway extends Square {
    private final int length;
    private final Direction orientation;

    public Hallway(Position start, int length, Direction orientation) {
        this.length = length;
        this.orientation = orientation;
        switch (orientation) {
            case NORTH:
                xMax = start.getX();
                xMin = xMax - length;
                yMin = start.getY();
                yMax = yMin + 1;
                break;
            case EAST:
                xMin = start.getX();
                xMax = xMin + 1;
                yMin = start.getY();
                yMax = yMin + length;
                break;
            case SOUTH:
                xMin = start.getX();
                xMax = xMin + length;
                yMin = start.getY();
                yMax = yMin + 1;
                break;
            case WEST:
                xMin = start.getX();
                xMax = xMin + 1;
                yMax = start.getY();
                yMin = yMax - length;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + orientation);
        }
    }

    public List<Direction> getEdges() {
        if (orientation == SOUTH || orientation == NORTH) {
            return List.of(EAST, WEST);
        } else {
            return List.of(NORTH, SOUTH);
        }
    }

}
