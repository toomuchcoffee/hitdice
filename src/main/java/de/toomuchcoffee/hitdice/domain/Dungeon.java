package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import static de.toomuchcoffee.hitdice.domain.Event.EMPTY_EVENT;
import static de.toomuchcoffee.hitdice.domain.EventType.EXPLORED;
import static java.lang.Math.abs;
import static java.lang.System.lineSeparator;

@Getter
@Setter
public class Dungeon {
    private int size;

    private Event[][] eventMap;

    private int posX;
    private int posY;

    public Dungeon(int size) {
        this.size = size;
        eventMap = new Event[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                eventMap[x][y] = EMPTY_EVENT;
            }
        }
    }

    public Position explore(Direction direction) {
        switch (direction) {
            case NORTH: {
                if (posY > 0) {
                    posY--;
                }
                break;
            }
            case EAST: {
                if (posX < size - 1) {
                    posX++;
                }
                break;
            }
            case SOUTH: {
                if (posY < size - 1) {
                    posY++;
                }
                break;
            }
            case WEST: {
                if (posX > 0) {
                    posX--;
                }
                break;
            }
            default: throw new IllegalArgumentException("invalid direction: " + direction);
        }
        return new Position(this.posX, this.posY);
    }

    public String getMap() {
        StringBuilder builder = new StringBuilder();
        builder.append("+");
        for (int i = 0; i < size; i++) {
            builder.append("---");
        }
        builder.append("+");
        for (int y = 0; y < size; y++) {
            builder.append(lineSeparator());
            builder.append("|");
            for (int x = 0; x < size; x++) {
                if (x == posX && y == posY) {
                    builder.append("(#)");
                } else if (abs(x - posX) < 2 && abs(y - posY) < 2 || eventMap[x][y].getType().equals(EXPLORED)) {
                    builder.append(" ").append(eventMap[x][y].getType().getSymbol()).append(" ");
                } else {
                    builder.append(" ? ");
                }
            }
            builder.append("|");
        }
        builder.append(lineSeparator());
        builder.append("+");
        for (int i = 0; i < size; i++) {
            builder.append("---");
        }
        builder.append("+");
        return builder.toString();
    }

    public void setPosition(Position pos) {
        this.posX = pos.getX();
        this.posY = pos.getY();
    }

    public Position getPosition() {
        return new Position(this.posX, this.posY);
    }

    public Event getPoi(Position position) {
        return eventMap[position.getX()][position.getY()];
    }
}
