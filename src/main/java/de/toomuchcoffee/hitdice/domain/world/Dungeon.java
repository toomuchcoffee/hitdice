package de.toomuchcoffee.hitdice.domain.world;

import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.abs;

@Getter
@Setter
public class Dungeon {
    private int size;

    private Event[][] eventMap;
    private boolean[][] visited;

    private int posX;
    private int posY;

    public Dungeon(int size) {
        this.size = size;
        eventMap = new Event[size][size];
        visited = new boolean[size][size];
    }

    public Position move(Direction direction) {
        switch (direction) {
            case NORTH: {
                if (posX > 0) {
                    posX--;
                }
                visit();
                break;
            }
            case EAST: {
                if (posY < size - 1) {
                    posY++;
                }
                visit();
                break;
            }
            case SOUTH: {
                if (posX < size - 1) {
                    posX++;
                }
                visit();
                break;
            }
            case WEST: {
                if (posY > 0) {
                    posY--;
                }
                visit();
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
                if (abs(x - posX) < 2 && abs(y - posY) < 2 || visited[x][y]) {
                    Event event = eventMap[x][y];
                    view[x][y] = event == null ? null : event.getEventType().getSymbol();
                } else {
                    view[x][y] = "question-circle unexplored";
                }
            }
        }

        view[posX][posY] = "user-circle hero";

        return view;
    }

    private void visit() {
        visited[posX][posY] = true;
    }

    public void setPosition(Position pos) {
        this.posX = pos.getX();
        this.posY = pos.getY();
        visit();
    }

    @VisibleForTesting
    public Position getPosition() {
        return new Position(this.posX, this.posY);
    }

    public Event getEvent(Position position) {
        return eventMap[position.getX()][position.getY()];
    }
}
