package de.toomuchcoffee.hitdice.domain.world;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Room extends Square {
    public Room(int startX, int startY, int width, int height, Direction entrance) {
        switch (entrance) {
            case NORTH:
                xMin = startX;
                xMax = xMin + width;
                yMin = startY - height / 2;
                yMax = yMin + height;
                break;
            case EAST:
                xMin = startX - width / 2;
                xMax = xMin + width;
                yMax = startY;
                yMin = yMax - height;
                break;
            case SOUTH:
                xMax = startX;
                xMin = xMax - width;
                yMin = startY - height / 2;
                yMax = yMin + height;
                break;
            case WEST:
                xMin = startX - width / 2;
                xMax = xMin + width;
                yMin = startY;
                yMax = yMin + height;
                break;
            default:
                throw new IllegalArgumentException("Unexpected argument: " + entrance);
        }
    }

    public Room(int startX, int startY, int width, int height) {
        xMin = startX - width / 2;
        xMax = xMin + width;
        yMin = startY - height / 2;
        yMax = yMin + height;
    }
}
