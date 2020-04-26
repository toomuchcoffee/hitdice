package de.toomuchcoffee.hitdice.service.dungeonmaker;

public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    public Orientation opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            default:
                throw new IllegalStateException("Unexpected value");
        }
    }
}
