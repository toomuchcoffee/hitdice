package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Poi.PoiType;

@Getter
@Setter
public class World implements Serializable {
    private static final long serialVersionUID = -9180826864067510787L;

    private int size;

    private Poi[][] poiMap;

    private int posX;
    private int posY;

    private Position getAnyPosition() {
        Random random = new Random();
        return new Position(random.nextInt(size), random.nextInt(size));
    }

    public World(int size) {
        this.size = size;
        poiMap = new Poi[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                poiMap[x][y] = new Poi(PoiType.EMPTY);
            }
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

    public Poi getPoi() {
        return poiMap[posX][posY];
    }
}
