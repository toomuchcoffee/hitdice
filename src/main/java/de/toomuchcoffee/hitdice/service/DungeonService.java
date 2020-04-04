package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Direction;
import de.toomuchcoffee.hitdice.domain.Dungeon;
import de.toomuchcoffee.hitdice.domain.Event;
import de.toomuchcoffee.hitdice.domain.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static de.toomuchcoffee.hitdice.service.Dice.D20;

@Service
@RequiredArgsConstructor
public class DungeonService {

    private static final Random RANDOM = new Random();
    private final TreasureService treasureService;
    private final MonsterService monsterService;
    private final PotionService potionService;

    public Dungeon create(int size) {
        Dungeon dungeon = new Dungeon(size);
        initPois(dungeon);
        Position start = getAnyUnoccupiedPosition(dungeon);
        dungeon.setPosition(start);
        markAsVisited(dungeon);
        return dungeon;
    }

    public Optional<Event> explore(Direction direction, Dungeon dungeon) {
        Position position = dungeon.explore(direction);
        return Optional.ofNullable(dungeon.getEvent(position));
    }

    public Position getAnyUnoccupiedPosition(Dungeon dungeon) {
        if (dungeon.getSize() == 1) {
            return new Position(0, 0);
        }

        Position pos;
        do {
            pos = new Position(RANDOM.nextInt(dungeon.getSize()), RANDOM.nextInt(dungeon.getSize()));
        } while (dungeon.getEventMap()[pos.getX()][pos.getY()] != null);
        return pos;
    }

    public void markAsVisited(Dungeon dungeon) {
        dungeon.getVisited()[dungeon.getPosX()][dungeon.getPosY()] = true;
        dungeon.getEventMap()[dungeon.getPosX()][dungeon.getPosY()] = null;
    }

    private void initPois(Dungeon dungeon) {
        for (int x = 0; x < dungeon.getSize(); x++) {
            for (int y = 0; y < dungeon.getSize(); y++) {
                dungeon.getEventMap()[x][y] = createEvent();
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getEventMap()[door.getX()][door.getY()] = Event.MAGIC_DOOR_EVENT;
    }

    private Event createEvent() {
        switch (D20.roll()) {
            case 1:
            case 2:
                return potionService.createPotion();
            case 3:
                return treasureService.createTreasure();
            case 4:
            case 5:
                return monsterService.createMonster();
            default:
                return null;
        }
    }

}
