package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;

@Service
@RequiredArgsConstructor
public class DungeonService {

    private final Random random;
    private final TreasureService treasureService;
    private final MonsterService monsterService;
    private final PotionService potionService;

    public Dungeon create(int heroLevel) {
        int size = new Random().nextInt(heroLevel + 4) + 5;
        Dungeon dungeon = new Dungeon(size);
        initPois(dungeon, heroLevel);
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
            pos = new Position(random.nextInt(dungeon.getSize()), random.nextInt(dungeon.getSize()));
        } while (dungeon.getEventMap()[pos.getX()][pos.getY()] != null);
        return pos;
    }

    public void markAsVisited(Dungeon dungeon) {
        dungeon.getVisited()[dungeon.getPosX()][dungeon.getPosY()] = true;
        dungeon.getEventMap()[dungeon.getPosX()][dungeon.getPosY()] = null;
    }

    private void initPois(Dungeon dungeon, int heroLevel) {
        List<MonsterTemplate> monsterTemplates = monsterService.findTemplates(heroLevel);
        for (int x = 0; x < dungeon.getSize(); x++) {
            for (int y = 0; y < dungeon.getSize(); y++) {
                dungeon.getEventMap()[x][y] = createEvent(monsterTemplates);
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getEventMap()[door.getX()][door.getY()] = Event.MAGIC_DOOR_EVENT;
    }

    private Event createEvent(List<MonsterTemplate> monsterTemplates) {
        switch (D20.roll()) {
            case 1:
                return potionService.createPotion();
            case 2:
                return treasureService.createTreasure();
            case 3:
            case 4:
                return monsterService.createMonster(monsterTemplates);
            default:
                return null;
        }
    }

}
