package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.*;
import de.toomuchcoffee.hitdice.factories.MonsterFactory;
import de.toomuchcoffee.hitdice.factories.TreasureFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;
import static de.toomuchcoffee.hitdice.domain.Event.*;
import static de.toomuchcoffee.hitdice.domain.EventType.*;

@Service
public class DungeonService {

    private static final Random RANDOM = new Random();

    public Dungeon create(int size) {
        Dungeon dungeon = new Dungeon(size);
        initPois(dungeon);
        Position start = getAnyUnoccupiedPosition(dungeon);
        dungeon.setPosition(start);
        markAsVisited(dungeon);
        return dungeon;
    }

    public Event explore(Direction direction, Dungeon dungeon, Hero hero) {
        Position position = dungeon.explore(direction);
        return dungeon.getPoi(position);
    }

    public Position getAnyUnoccupiedPosition(Dungeon dungeon) {
        if (dungeon.getSize() == 1) {
            return new Position(0, 0);
        }

        Position pos;
        do {
            pos = new Position(RANDOM.nextInt(dungeon.getSize()), RANDOM.nextInt(dungeon.getSize()));
        } while (dungeon.getEventMap()[pos.getX()][pos.getY()].getType().isOccupied());
        return pos;
    }

    public void markAsVisited(Dungeon dungeon) {
        dungeon.getEventMap()[dungeon.getPosX()][dungeon.getPosY()] = EXPLORED_EVENT;
    }

    public void collectTreasure(Hero hero, Treasure treasure) {
        if (treasure instanceof Armor) {
            hero.setArmor((Armor) treasure);
        } else if (treasure instanceof Weapon) {
            hero.setWeapon((Weapon) treasure);
        }
    }

    public void drinkPotion(Hero hero, Potion potion) {
        hero.setCurrentStamina(Math.min(hero.getCurrentStamina() + potion.getPower(), hero.getStamina()));
    }

    private void initPois(Dungeon dungeon) {
        for (int x = 0; x < dungeon.getSize(); x++) {
            for (int y = 0; y < dungeon.getSize(); y++) {
                dungeon.getEventMap()[x][y] = createEvent();
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getEventMap()[door.getX()][door.getY()] = MAGIC_DOOR_EVENT;
    }

    private Event createEvent() {
        int d = D20.roll();
        if (d > 17) {
            return new Event(POTION, new Potion(D6.roll(2)));
        } else if (d > 14) {
            return new Event(TREASURE, TreasureFactory.createTreasure());
        } else if (d > 11) {
            return new Event(MONSTER, MonsterFactory.createMonster());
        } else {
            return EMPTY_EVENT;
        }
    }

}
