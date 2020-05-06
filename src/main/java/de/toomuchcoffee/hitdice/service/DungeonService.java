package de.toomuchcoffee.hitdice.service;

import com.google.common.annotations.VisibleForTesting;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.event.factory.MonsterFactory;
import de.toomuchcoffee.hitdice.domain.world.*;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.*;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

@Service
@RequiredArgsConstructor
public class DungeonService {

    private final Random random;
    private final EventService eventService;

    public Dungeon create(int heroLevel) {
        Tile[][] tiles = createTiles();
        Dungeon dungeon = new Dungeon(tiles);
        addEvents(dungeon, heroLevel, newHashSet(ROOM));
        Position door = getAnyUnoccupiedPosition(dungeon, newHashSet(ROOM));
        dungeon.getTiles()[door.getX()][door.getY()] = new Tile(MAGIC_DOOR);
        Position start = getAnyUnoccupiedPosition(dungeon, newHashSet(ROOM, HALLWAY));
        dungeon.setPosition(start);
        return dungeon;
    }

    public Tile move(Dungeon dungeon, Direction direction) {
        Position position = dungeon.move(direction);
        return dungeon.getTile(position);
    }

    public Position getAnyUnoccupiedPosition(Dungeon dungeon, Set<TileType> filter) {
        if (dungeon.getWidth() == 1) {
            return Position.of(0, 0);
        }

        Position pos;
        do {
            pos = Position.of(random.nextInt(dungeon.getWidth()), random.nextInt(dungeon.getHeight()));
        } while (pos.equals(dungeon.getPosition()) || dungeon.getTile(pos).isOccupied() || !filter.contains(dungeon.getTile(pos).getType()));
        return pos;
    }

    public void clear(Dungeon dungeon) {
        dungeon.getTile(dungeon.getPosition()).setOccupant(null);
    }

    public Optional<Item> getTreasure(Dungeon dungeon) {
        return Optional.ofNullable(dungeon.getTile(dungeon.getPosition()).getOccupant())
                .filter(e -> e instanceof Item)
                .map(i -> (Item) i);
    }

    public Tile[][] createTiles() {
        int dungeonSize = 25;
        int featureCount = 2 + random.nextInt(6);

        List<Square> squares = createSquares(dungeonSize, featureCount);

        Square bounds = bounds(squares);
        normalizeSquares(squares, bounds);

        return createTiles(squares, bounds);
    }

    private void addEvents(Dungeon dungeon, int heroLevel, Set<TileType> filter) {
        List<MonsterFactory> factories = eventService.findFactories(heroLevel);
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                Tile tile = dungeon.getTiles()[x][y];
                if (!tile.isOccupied() && filter.contains(tile.getType())) {
                    eventService.createEvent(factories).ifPresent(tile::setOccupant);
                }
            }
        }
    }

    private List<Square> createSquares(int dungeonSize, int featureCount) {
        // Fill the whole map with solid earth
        Square map = new Square(0, 0, dungeonSize, dungeonSize);
        List<Square> squares = new ArrayList<>();

        // Dig out a single room in the centre of the map
        Room room = createRoom(Position.of(dungeonSize / 2, dungeonSize / 2));
        squares.add(room);

        while (featureCount > 0) {
            // Pick a wall of any room
            Square picked = squares.get(random.nextInt(squares.size()));
            Direction orientation;
            if (picked instanceof Hallway) {
                orientation = ((Hallway) picked).getEdges().get(random.nextInt(2));
            } else {
                orientation = Direction.values()[random.nextInt(Direction.values().length)];
            }
            Position wallBreakThrough = picked.edgeCenter(orientation);

            // Decide upon a new feature to build
            Hallway hallway = createHallway(wallBreakThrough.getX(), wallBreakThrough.getY(), orientation);
            Position hallwayEnd = wallBreakThrough.shift(orientation, hallway.getLength());
            Room newRoom = createRoom(hallwayEnd, orientation.opposite());

            // See if there is room to add the new feature through the chosen wall
            // If yes, continue. If no, go back to step 3
            boolean fits = map.contains(hallway) &&
                    map.contains(newRoom) &&
                    squares.stream()
                            .filter(square -> !square.equals(picked))
                            .noneMatch(square -> square.intersects(hallway) || square.intersects(newRoom));

            // Add the feature through the chosen wall
            if (fits) {
                squares.add(hallway);
                squares.add(newRoom);
                featureCount--;
            }
            // Go back to step 3, until the dungeon is complete
        }
        return squares;
    }

    private void normalizeSquares(List<Square> squares, Square bounds) {
        int xOffset = bounds.getXMin();
        int yOffset = bounds.getYMin();

        List<Square> list = newArrayList(squares);
        list.add(bounds);
        for (Square square : list) {
            square.setXMin(square.getXMin() - xOffset);
            square.setXMax(square.getXMax() - xOffset);
            square.setYMin(square.getYMin() - yOffset);
            square.setYMax(square.getYMax() - yOffset);
        }
    }

    @VisibleForTesting
    Square bounds(List<Square> squares) {
        int xMin = MAX_VALUE, xMax = MIN_VALUE, yMin = MAX_VALUE, yMax = MIN_VALUE;
        for (Square square : squares) {
            if (square.getXMin() < xMin) {
                xMin = square.getXMin();
            }
            if (square.getYMin() < yMin) {
                yMin = square.getYMin();
            }
            if (square.getXMax() > xMax) {
                xMax = square.getXMax();
            }
            if (square.getYMax() > yMax) {
                yMax = square.getYMax();
            }
        }
        return new Square(xMin, yMin, xMax, yMax);
    }

    private Tile[][] createTiles(List<? extends Square> squares, Square bounds) {
        Tile[][] tiles = new Tile[bounds.getXMax()][bounds.getYMax()];
        for (int x = bounds.getXMin(); x < bounds.getXMax(); x++) {
            for (int y = bounds.getYMin(); y < bounds.getYMax(); y++) {
                tiles[x][y] = new Tile(SOIL);
            }
        }
        for (Square square : squares) {
            for (int x = square.getXMin(); x < square.getXMax(); x++) {
                for (int y = square.getYMin(); y < square.getYMax(); y++) {
                    if (square instanceof Room) {
                        tiles[x][y] = new Tile(ROOM);
                    } else if (square instanceof Hallway) {
                        tiles[x][y] = new Tile(HALLWAY);
                    }
                }
            }
        }
        return tiles;
    }

    private Room createRoom(Position point, Direction entrance) {
        int width = 3 + 2 * random.nextInt(3);
        int height = 3 + 2 * random.nextInt(3);
        return new Room(point.getX(), point.getY(), width, height, entrance);
    }

    private Room createRoom(Position point) {
        int width = 3 + 2 * random.nextInt(3);
        int height = 3 + 2 * random.nextInt(3);
        return new Room(point.getX(), point.getY(), width, height);
    }

    private Hallway createHallway(int startX, int startY, Direction orientation) {
        int length = 3 + 2 * random.nextInt(4);
        return new Hallway(Position.of(startX, startY), length, orientation);
    }

}
