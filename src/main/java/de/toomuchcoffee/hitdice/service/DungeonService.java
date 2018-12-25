package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.Main;
import de.toomuchcoffee.hitdice.domain.*;
import de.toomuchcoffee.hitdice.factories.HeroFactory;
import de.toomuchcoffee.hitdice.factories.PoiFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

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

    public boolean explore(Direction direction, Dungeon dungeon, Hero hero) {
        boolean result = dungeon.explore(direction);
        checkIncident(dungeon, hero);
        return result;
    }

    private void checkIncident(Dungeon dungeon, Hero hero) {
        Poi poi = dungeon.getPoi();
        switch (poi.getType()) {
            case MONSTER: {
                Combat combat = new Combat(hero, (Monster) poi.getObject());
                if (combat.fight()) {
                    HeroFactory.gainExperience(hero, combat.getExperienceValue());
                    markAsVisited(dungeon);
                } else {
                    dungeon.setPosition(getAnyUnoccupiedPosition(dungeon));
                }
                break;
            }
            case POTION: {
                int recovery = (Integer) poi.getObject();
                //Main.draw("You found a healing potion and recover %d of stamina.", recovery);
                hero.recoverStaminaBy(recovery);
                markAsVisited(dungeon);
                break;
            }
            case TREASURE: {
                Treasure treasure = (Treasure) poi.getObject();
                //Main.draw("You found a %s", treasure.getName());
                //printInventory();
                if (Main.confirm("Do you want to take the " + treasure.getName() + "?")) {
                    hero.stash(treasure);
                }
                markAsVisited(dungeon);
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
        } while (dungeon.getPoiMap()[pos.x][pos.y].isOccupied());
        return pos;
    }

    private void markAsVisited(Dungeon dungeon) {
        dungeon.getPoiMap()[dungeon.getPosX()][dungeon.getPosY()] = new Poi(Poi.PoiType.EXPLORED);
    }

    private void initPois(Dungeon dungeon) {
        for (int x = 0; x < dungeon.getSize(); x++) {
            for (int y = 0; y < dungeon.getSize(); y++) {
                dungeon.getPoiMap()[x][y] = PoiFactory.createPoi();
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getPoiMap()[door.x][door.y] = new Poi(Poi.PoiType.MAGIC_DOOR);
    }

}
