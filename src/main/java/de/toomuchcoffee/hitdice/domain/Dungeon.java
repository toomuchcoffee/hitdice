package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import static de.toomuchcoffee.hitdice.domain.Poi.PoiType;

@Getter
@Setter
public class Dungeon {
    private int size;

    private Poi[][] poiMap;

    private int posX;
    private int posY;

    public Dungeon(int size) {
        this.size = size;
        poiMap = new Poi[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                poiMap[x][y] = new Poi(PoiType.EMPTY);
            }
        }
    }

    public boolean explore(Direction direction) {
        switch (direction) {
            case NORTH: {
                if (posY > 0) {
                    posY--;
                    return true;
                }
                return false;
            }
            case EAST: {
                if (posX < size - 1) {
                    posX++;
                    return true;
                }
                return false;
            }
            case SOUTH: {
                if (posY < size - 1) {
                    posY++;
                    return true;
                }
                return false;
            }
            case WEST: {
                if (posX > 0) {
                    posX--;
                    return true;
                }
                return false;
            }
            default: throw new IllegalArgumentException("invalid direction: " + direction);
        }
    }

    public String getMap() {
        StringBuilder builder = new StringBuilder();
        builder.append("+");
        for (int i = 0; i < size; i++) {
            builder.append("---");
        }
        builder.append("+");
        for (int y = 0; y < size; y++) {
            builder.append(System.lineSeparator());
            builder.append("|");
            for (int x = 0; x < size; x++) {
                if (x == posX && y == posY) {
                    builder.append("(#)");
                } else if (Math.abs(x - posX) < 2 && Math.abs(y - posY) < 2 || poiMap[x][y].getType().equals(PoiType.EXPLORED)) {
                    builder.append(" ").append(poiMap[x][y].getSymbol()).append(" ");
                } else {
                    builder.append(" ? ");
                }
            }
            builder.append("|");
        }
        builder.append(System.lineSeparator());
        builder.append("+");
        for (int i = 0; i < size; i++) {
            builder.append("---");
        }
        builder.append("+");
        return builder.toString();
    }

    public void setPosition(Position pos) {
        this.posX = pos.x;
        this.posY = pos.y;
    }

    public Position getPosition() {
        return new Position(this.posX, this.posY);
    }

    public Poi getPoi() {
        return poiMap[posX][posY];
    }
}
