package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.Main;
import de.toomuchcoffee.hitdice.factories.PoiFactory;

import java.io.Serializable;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Poi.PoiType;

public class World implements Serializable {
    private static final long serialVersionUID = -9180826864067510787L;

    private int size;

    private Poi[][] poiMap;

    private int posX;
    private int posY;

    public static class Position {
        public int x;
        public int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private Position getAnyPosition() {
        Random random = new Random();
        return new Position(random.nextInt(size), random.nextInt(size));
    }

    public Position getAnyUnoccupiedPosition() {
        if (size == 1) {
            return new Position(0, 0);
        }

        Position pos;
        do {
            pos = getAnyPosition();
        } while (poiMap[pos.x][pos.y].isOccupied());
        return pos;
    }

    public void markAsVisited() {
        this.poiMap[this.posX][this.posY] = new Poi(PoiType.EXPLORED);
    }

    public World(int size) {
        this.size = size;
        initPois();
    }

    private void initPois() {
        this.poiMap = new Poi[size][size];
        for (int x=0; x<size; x++) {
            for (int y=0; y<size; y++) {
                poiMap[x][y] = PoiFactory.createPoi();
            }
        }
        Position door = getAnyUnoccupiedPosition();
        poiMap[door.x][door.y] = new Poi(PoiType.MAGIC_DOOR);
    }

    public String getMap() {
        StringBuilder builder = new StringBuilder();
        builder.append("+");
        for(int i=0; i<size; i++ ) {
            builder.append("---");
        }
        builder.append("+");
        for (int y=0; y<size; y++) {
            builder.append(System.lineSeparator());
            builder.append("|");
            for (int x=0; x<size; x++) {
                if (x==posX && y==posY) {
                    builder.append("(#)");
                } else if (Math.abs(x-posX)<2 && Math.abs(y-posY)<2 || poiMap[x][y].getType().equals(PoiType.EXPLORED)) {
                    builder.append(" "+poiMap[x][y].getSymbol()+" ");
                } else {
                    builder.append(" ? ");
                }
            }
            builder.append("|");
        }
        builder.append(System.lineSeparator());
        builder.append("+");
        for(int i=0; i<size; i++ ) {
            builder.append("---");
        }
        builder.append("+");
        return builder.toString();
    }

    public void goNorth() {
        if (posY > 0) {
            this.posY--;
        } else {
            bump();
        }
    }

    public void goWest() {
        if (posX > 0) {
            this.posX--;
        } else {
            bump();
        }
    }

    public void goEast() {
        if (posX < size-1) {
            this.posX++;
        } else {
            bump();
        }
    }

    public void goSouth() {
        if (posY < size-1) {
            this.posY++;
        } else {
            bump();
        }
    }

    private void bump() {
        Main.draw("Ouch! You reached the end of the world and it hurt. Go into another direction.");
    }

    public void setPosition(Position pos) {
        this.posX = pos.x;
        this.posY = pos.y;
    }

    public void setAnyUnoccupiedPosition() {
        setPosition(getAnyUnoccupiedPosition());
    }

    public Poi getPoi() {
        return poiMap[posX][posY];
    }
}
