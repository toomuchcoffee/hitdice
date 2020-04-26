package de.toomuchcoffee.hitdice.domain.world;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public Direction opposite() {
        return values()[(ordinal() + 2) % 4];
    }
}
