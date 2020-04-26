package de.toomuchcoffee.hitdice.service.dungeonmaker;

import lombok.Getter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.service.dungeonmaker.Orientation.*;

@Getter
public class Hallway extends Square {
    private int length;
    private Orientation orientation;

    public Hallway(Point start, int length, Orientation orientation) {
        this.length = length;
        this.orientation = orientation;
        switch (orientation) {
            case NORTH:
                xMin = start.getX();
                xMax = xMin + 1;
                yMin = start.getY();
                yMax = yMin + length;
                break;
            case SOUTH:
                xMin = start.getX();
                xMax = xMin + 1;
                yMax = start.getY();
                yMin = yMax - length;
                break;
            case EAST:
                xMin = start.getX();
                xMax = xMin + length;
                yMin = start.getY();
                yMax = yMin + 1;
                break;
            case WEST:
                xMax = start.getX();
                xMin = xMax - length;
                yMin = start.getY();
                yMax = yMin + 1;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + orientation);
        }
    }

    public List<Orientation> getEdges() {
        if (orientation == SOUTH || orientation == NORTH) {
            return newArrayList(EAST, WEST);
        } else {
            return newArrayList(NORTH, SOUTH);
        }
    }

}
