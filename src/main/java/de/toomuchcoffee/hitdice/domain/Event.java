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
        return type.getSymbol();
    }

    public Object getObject() {
        return obj;
    }

    public EventType getType() {
        return type;
    }

}
