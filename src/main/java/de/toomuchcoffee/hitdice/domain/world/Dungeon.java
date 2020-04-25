package de.toomuchcoffee.hitdice.domain.world;

import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.SOIL;
import static java.lang.Math.abs;

@Getter
@Setter
public class Dungeon {
    private int size;

    private Tile[][] tiles;

    private int posX;
    private int posY;

    public Dungeon(int size) {
        this.size = size;
        tiles = new Tile[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tiles[x][y] = new Tile(ROOM);
            }
        }
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
                if (abs(x - posX) < 2 && abs(y - posY) < 2 || tiles[x][y].isVisited()) {
                    Event event = tiles[x][y].getEvent();
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
        tiles[posX][posY].setVisited(true);
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

    public Optional<Event> getEvent(Position position) {
        return Optional.ofNullable(tiles[position.getX()][position.getY()].getEvent());
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Tile {
        private final TileType type;
        private Event event;
        private boolean visited;

        public boolean isOccupied() {
            return type == SOIL || event != null;
        }
    }

    enum TileType {
        SOIL, ROOM, HALLWAY
    }
}
