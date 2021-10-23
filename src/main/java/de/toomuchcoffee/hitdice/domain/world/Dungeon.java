package de.toomuchcoffee.hitdice.domain.world;

import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.MAGIC_DOOR;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.SOIL;
import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class Dungeon {
    private Tile[][] tiles;

    @Getter
    private Position position;

    public Dungeon(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public List<Tile> getFlattenedTiles() {
        return Stream.of(tiles).flatMap(Arrays::stream).collect(toList());
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public Position move(Direction direction) {
        Position newPosition = position.shift(direction);
        if (isAllowed(newPosition) && getTile(newPosition).isVisitable()) {
            position = newPosition;
            visit();
        }
        return position;
    }

    private boolean isAllowed(Position position) {
        return position.getX() >= 0 && position.getX() < getWidth() && position.getY() >= 0 && position.getY() < getHeight();
    }

    public String[][] getMap() {
        String[][] view = new String[getWidth()][getHeight()];

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (getTile(Position.of(x, y)).isExplored()) {
                    if (tiles[x][y].getType() == SOIL) {
                        view[x][y] = "square-full";
                    } else if (tiles[x][y].getType() == MAGIC_DOOR) {
                        view[x][y] = "dungeon";
                    } else {
                        Object o = tiles[x][y].getOccupant();
                        view[x][y] = o == null ? null : getSymbol(o);
                    }
                } else {
                    view[x][y] = "question-circle unexplored";
                }
            }
        }

        view[position.getX()][position.getY()] = "user-circle hero";

        return view;
    }

    private String getSymbol(Object o) {
        if (o instanceof Monster) {
            return "pastafarianism";
        }
        if (o instanceof Potion) {
            return "flask";
        }
        return "coins";
    }

    public Tile getTile(Position position) {
        return tiles[position.getX()][position.getY()];
    }

    private List<Tile> getTilesWithinView(Position position) {
        List<Tile> tiles = new ArrayList<>();
        int viewRange = 1;
        int minX = Math.max(0, position.getX() - viewRange);
        int maxX = Math.min(getWidth() - 1, position.getX() + viewRange);
        int minY = Math.max(0, position.getY() - viewRange);
        int maxY = Math.min(getHeight() - 1, position.getY() + viewRange);
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

    public boolean isExplored() {
        return Arrays.stream(tiles)
                .flatMap(Arrays::stream)
                .allMatch(t -> !t.isVisitable() || t.isExplored());
    }

    public void setPosition(Position pos) {
        this.position = pos;
        visit();
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Tile {
        private final TileType type;
        private Object occupant;
        private boolean explored;

        public boolean isOccupied() {
            return type == SOIL || type == MAGIC_DOOR || occupant != null;
        }

        public boolean isVisitable() {
            return type != SOIL;
        }
    }

    public enum TileType {
        SOIL, ROOM, HALLWAY, MAGIC_DOOR
    }
}
