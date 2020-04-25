package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.domain.world.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.MAGIC_DOOR;

@Service
@RequiredArgsConstructor
public class DungeonService {

    private final Random random;
    private final EventService eventService;

    public Dungeon create(int heroLevel) {
        int size = new Random().nextInt(heroLevel + 4) + 6;
        Dungeon dungeon = new Dungeon(size);
        initTiles(dungeon, heroLevel);
        Position start = getAnyUnoccupiedPosition(dungeon);
        dungeon.setPosition(start);
        return dungeon;
    }

    public Tile move(Dungeon dungeon, Direction direction) {
        Position position = dungeon.move(direction);
        return dungeon.getTile(position);
    }

    public Position getAnyUnoccupiedPosition(Dungeon dungeon) {
        if (dungeon.getSize() == 1) {
            return Position.of(0, 0);
        }

        Position pos;
        do {
            pos = Position.of(random.nextInt(dungeon.getSize()), random.nextInt(dungeon.getSize()));
        } while (dungeon.getTiles()[pos.getX()][pos.getY()].isOccupied());
        return pos;
    }

    public void clear(Dungeon dungeon) {
        dungeon.getTile(dungeon.getPosition()).setEvent(null);
    }

    private void initTiles(Dungeon dungeon, int heroLevel) {
        List<MonsterTemplate> monsterTemplates = eventService.findTemplates(heroLevel);
        for (int x = 0; x < dungeon.getSize(); x++) {
            for (int y = 0; y < dungeon.getSize(); y++) {
                Tile tile = dungeon.getTiles()[x][y];
                if (!tile.isOccupied()) {
                    eventService.createEvent(monsterTemplates).ifPresent(tile::setEvent);
                }
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getTiles()[door.getX()][door.getY()] = new Tile(MAGIC_DOOR);
    }

}
