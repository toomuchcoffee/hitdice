package de.toomuchcoffee.hitdice.domain;

import java.io.Serializable;

public class Poi implements Serializable {

    private static final long serialVersionUID = 8675100304180747456L;

    private PoiType type;
    private Object obj;

    public Poi(PoiType type) {
        this.type = type;
    }

    public Poi(PoiType type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public boolean isOccupied() {
        return obj != null;
    }

    public String getSymbol() {
        return type.symbol;
    }

    public Object getObject() {
        return obj;
    }

    public PoiType getType() {
        return type;
    }

    public enum PoiType {
        EMPTY(" ", false),
        MONSTER("¥", true),
        POTION("†", true),
        TREASURE("$", true),
        EXPLORED(" ", false),
        MAGIC_DOOR("§", true);

        String symbol;
        boolean occupied;

        PoiType(String symbol, boolean occupied) {
            this.symbol = symbol;
            this.occupied = occupied;
        }
    }

}
