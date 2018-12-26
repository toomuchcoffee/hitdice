package de.toomuchcoffee.hitdice.domain;

public class Event {

    private EventType type;
    private Object obj;

    public Event(EventType type) {
        this.type = type;
    }

    public Event(EventType type, Object obj) {
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

    public EventType getType() {
        return type;
    }

    public enum EventType {
        EMPTY(" ", false),
        MONSTER("¥", true),
        POTION("†", true),
        TREASURE("$", true),
        EXPLORED(" ", false),
        MAGIC_DOOR("§", true);

        String symbol;
        boolean occupied;

        EventType(String symbol, boolean occupied) {
            this.symbol = symbol;
            this.occupied = occupied;
        }
    }

}
