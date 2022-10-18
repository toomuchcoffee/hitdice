package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.world.*;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.*;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

@Service
@RequiredArgsConstructor
public class DungeonCreationService {

    private final Random random;
    private final EventService eventService;

    public Dungeon create(int heroLevel) {
        Tile[][] tiles = createTiles();
        Dungeon dungeon = new Dungeon(tiles);
        addEvents(dungeon, heroLevel);
        dungeon.getAnyUnoccupiedPosition(ROOM).ifPresent(p -> dungeon.putTile(new Tile(p, MAGIC_DOOR)));
        dungeon.getAnyUnoccupiedPosition(ROOM, HALLWAY).ifPresent(dungeon::setPosition);
        return dungeon;
    }

    public Tile[][] createTiles() {
        int dungeonSize = 18;
        int featureCount = 2 + random.nextInt(6);

        List<Square> squares = createSquares(dungeonSize, featureCount);

        Square bounds = bounds(squares);
        normalizeSquares(squares, bounds);

        return createTiles(squares, bounds);
    }

    private void addEvents(Dungeon dungeon, int heroLevel) {
        dungeon.getFlattenedTiles().stream()
                .filter(t -> t.getType() == ROOM)
                .filter(Tile::isUnoccupied)
                .forEach(tile -> eventService.createEvent(heroLevel).ifPresent(tile::setOccupant));
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

        List<Square> list = new ArrayList<>(squares);
        list.add(bounds);
        for (Square square : list) {
            square.setXMin(square.getXMin() - xOffset);
            square.setXMax(square.getXMax() - xOffset);
            square.setYMin(square.getYMin() - yOffset);
            square.setYMax(square.getYMax() - yOffset);
        }
    }

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
                tiles[x][y] = new Tile(Position.of(x, y), SOIL);
            }
        }
        for (Square square : squares) {
            for (int x = square.getXMin(); x < square.getXMax(); x++) {
                for (int y = square.getYMin(); y < square.getYMax(); y++) {
                    if (square instanceof Room) {
                        tiles[x][y] = new Tile(Position.of(x, y), ROOM);
                    } else if (square instanceof Hallway) {
                        tiles[x][y] = new Tile(Position.of(x, y), HALLWAY);
                    }
                }
            }
        }
        return tiles;
    }

    private Room createRoom(Position point, Direction entrance) {
        int width = 2 + 2 * random.nextInt(3);
        int height = 2 + 2 * random.nextInt(3);
        return new Room(point.getX(), point.getY(), width, height, entrance);
    }

    private Room createRoom(Position point) {
        int width = 2 + 2 * random.nextInt(3);
        int height = 2 + 2 * random.nextInt(3);
        return new Room(point.getX(), point.getY(), width, height);
    }

    private Hallway createHallway(int startX, int startY, Direction orientation) {
        int length = 2 + 2 * random.nextInt(4);
        return new Hallway(Position.of(startX, startY), length, orientation);
    }

}
