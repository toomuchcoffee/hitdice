package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.*;
import de.toomuchcoffee.hitdice.factories.EventFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Event.EXPLORED_EVENT;
import static de.toomuchcoffee.hitdice.domain.Event.MAGIC_DOOR_EVENT;

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

    private void checkIncident(Dungeon dungeon, Hero hero) {
        Event event = null;
        switch (event.getType()) {
            case MONSTER: {
                break;
            }
            case POTION: {
                break;
            }
            case TREASURE: {
                break;
            }
            case MAGIC_DOOR: {
                //Main.draw("You see a strange mystical sign that someone has drawn on the floor" + lineSeparator() +
                //      "As you step on it suddenly everything around you dissolves into strange shapes and colors" + lineSeparator() +
                //    " only to reshape into a complete new surrounding!" + lineSeparator() +
                //  "You entered yet another world. Oh no!");
                create(RANDOM.nextInt(hero.getLevel() + 4) + 5);
                break;
            }
            case EMPTY: {
            }
            case EXPLORED:
            default: {
                //Main.draw("Pretty boring area here. Let's move on.");
                markAsVisited(dungeon);
            }
        }
    }

    private Position getAnyUnoccupiedPosition(Dungeon dungeon) {
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
                dungeon.getEventMap()[x][y] = EventFactory.create();
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getEventMap()[door.getX()][door.getY()] = MAGIC_DOOR_EVENT;
    }

}
