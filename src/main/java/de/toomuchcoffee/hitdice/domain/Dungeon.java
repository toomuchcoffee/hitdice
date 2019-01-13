package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import static de.toomuchcoffee.hitdice.domain.Event.EMPTY_EVENT;
import static de.toomuchcoffee.hitdice.domain.EventType.EXPLORED;
import static java.lang.Math.abs;

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
                if (posX > 0) {
                    posX--;
                }
                break;
            }
            case EAST: {
                if (posY < size - 1) {
                    posY++;
                }
                break;
            }
            case SOUTH: {
                if (posX < size - 1) {
                    posX++;
                }
                break;
            }
            case WEST: {
                if (posY > 0) {
                    posY--;
                }
                break;
            }
            default:
                throw new IllegalArgumentException("invalid direction: " + direction);
        }
        return new Position(this.posX, this.posY);
    }

    public String[][] getMap() {
        String[][] view = new String[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (abs(x - posX) < 2 && abs(y - posY) < 2 || eventMap[x][y].getType().equals(EXPLORED)) {
                    view[x][y] = eventMap[x][y].getType().getSymbol();
                } else {
                    view[x][y] = "question-circle unexplored";
                }
            }
        }

        view[posX][posY] = "user-circle hero";

        return view;
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
