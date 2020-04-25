package de.toomuchcoffee.hitdice.domain.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.ROOM;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.SOIL;

@Getter
@Setter
public class Dungeon {
    private int size;

    private Tile[][] tiles;

    @Getter
    private Position position;

    public Dungeon(int size) {
        this.size = size;
        tiles = new Tile[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == 0 || x == size - 1 || y == 0 || y == size - 1) {
                    tiles[x][y] = new Tile(SOIL);
                } else {
                    tiles[x][y] = new Tile(ROOM);
                }
            }
        }
    }

    public Position move(Direction direction) {
        Position newPosition = position.move(direction);
        if (getTile(newPosition).isVisitable() && isAllowed(newPosition)) {
            position = newPosition;
            visit();
        }
        return position;
    }

    private boolean isAllowed(Position position) {
        return position.getX() >= 0 && position.getX() < size && position.getY() >= 0 && position.getY() < size;
    }

    public String[][] getMap() {
        String[][] view = new String[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (getTile(Position.of(x, y)).isExplored()) {
                    if (tiles[x][y].getType() == SOIL) {
                        view[x][y] = "square-full";
                    } else {
                        Event event = tiles[x][y].getEvent();
                        view[x][y] = event == null ? null : event.getEventType().getSymbol();
                    }
                } else {
                    view[x][y] = "question-circle unexplored";
                }
            }
        }

        view[position.getX()][position.getY()] = "user-circle hero";

        return view;
    }

    public Tile getTile(Position position) {
        return tiles[position.getX()][position.getY()];
    }

    private List<Tile> getTilesWithinView(Position position) {
        List<Tile> tiles = new ArrayList<>();
        int viewRange = 1;
        int minX = Math.max(0, position.getX() - viewRange);
        int maxX = Math.min(size - 1, position.getX() + viewRange);
        int minY = Math.max(0, position.getY() - viewRange);
        int maxY = Math.min(size - 1, position.getY() + viewRange);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                tiles.add(getTile(Position.of(x, y)));
            }
        }
        return tiles;
    }

    private void visit() {
        getTilesWithinView(position).forEach(tile -> tile.setExplored(true));
    }

    public void setPosition(Position pos) {
        this.position = pos;
        visit();
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
        private boolean explored;

        public boolean isOccupied() {
            return type == SOIL || event != null;
        }

        public boolean isVisitable() {
            return type != SOIL;
        }
    }

    enum TileType {
        SOIL, ROOM, HALLWAY
    }
}
