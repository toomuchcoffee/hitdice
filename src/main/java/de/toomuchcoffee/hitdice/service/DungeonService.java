package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.Main;
import de.toomuchcoffee.hitdice.domain.*;
import de.toomuchcoffee.hitdice.factories.HeroFactory;
import de.toomuchcoffee.hitdice.factories.PoiFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DungeonService {

    public World create() {
        World world = new World(8);
        initPois(world);
        return world;
    }

    public boolean explore(Direction direction, World world, Hero hero) {
        boolean result;
        switch (direction) {
            case NORTH: result = goNorth(world); break;
            case EAST: result = goEast(world); break;
            case SOUTH: result = goSouth(world); break;
            case WEST: result = goWest(world); break;
            default: throw new IllegalArgumentException("invalid direction: " + direction);
        }
        checkIncident(world, hero);
        return result;
    }

    private boolean goNorth(World world) {
        if (world.getPosY() > 0) {
            world.setPosY(world.getPosY() - 1);
            return true;
        }
        return false;
    }

    private boolean goWest(World world) {
        if (world.getPosX() > 0) {
            world.setPosX(world.getPosX() - 1);
            return true;
        }
        return false;
    }

    private boolean goEast(World world) {
        if (world.getPosX() < world.getSize() - 1) {
            world.setPosX(world.getPosX() + 1);
            return true;
        }
        return false;
    }

    private boolean goSouth(World world) {
        if (world.getPosY() < world.getSize() - 1) {
            world.setPosY(world.getPosY() + 1);
            return true;
        }
        return false;
    }

    private void checkIncident(World world, Hero hero) {
        Poi poi = world.getPoi();
        switch (poi.getType()) {
            case MONSTER: {
                Combat combat = new Combat(hero, (Monster) poi.getObject());
                if (combat.fight()) {
                    HeroFactory.gainExperience(hero, combat.getExperienceValue());
                    markAsVisited(world);
                } else {
                    setAnyUnoccupiedPosition(world);
                }
                break;
            }
            case POTION: {
                int recovery = (Integer) poi.getObject();
                //Main.draw("You found a healing potion and recover %d of stamina.", recovery);
                hero.recoverStaminaBy(recovery);
                markAsVisited(world);
                break;
            }
            case TREASURE: {
                Treasure treasure = (Treasure) poi.getObject();
                //Main.draw("You found a %s", treasure.getName());
                //printInventory();
                if (Main.confirm("Do you want to take the " + treasure.getName() + "?")) {
                    hero.stash(treasure);
                }
                markAsVisited(world);
                break;
            }
            case EXPLORED: {
                //Main.draw("You've been here before. Turn the page and move on.");
                markAsVisited(world);
                break;
            }
            case MAGIC_DOOR: {
                //Main.draw("You see a strange mystical sign that someone has drawn on the floor" + lineSeparator() +
                //      "As you step on it suddenly everything around you dissolves into strange shapes and colors" + lineSeparator() +
                //    " only to reshape into a complete new surrounding!" + lineSeparator() +
                //  "You entered yet another world. Oh no!");
                enterNewWorld(world, hero);
                break;
            }
            case EMPTY: {
            }
            default: {
                //Main.draw("Pretty boring area here. Let's move on.");
                markAsVisited(world);
            }
        }
    }

    public void enterNewWorld(World world, Hero hero) {
        world = new World(new Random().nextInt(hero.getLevel() + 4) + 5);
        Position start = getAnyUnoccupiedPosition(world);
        world.setPosition(start);
        markAsVisited(world);
    }


    private Position getAnyPosition(World world) {
        Random random = new Random();
        return new Position(random.nextInt(world.getSize()), random.nextInt(world.getSize()));
    }

    public Position getAnyUnoccupiedPosition(World world) {
        if (world.getSize() == 1) {
            return new Position(0, 0);
        }

        Position pos;
        do {
            pos = getAnyPosition(world);
        } while (world.getPoiMap()[pos.x][pos.y].isOccupied());
        return pos;
    }

    private void markAsVisited(World world) {
        world.getPoiMap()[world.getPosX()][world.getPosY()] = new Poi(Poi.PoiType.EXPLORED);
    }

    private void initPois(World world) {
        for (int x = 0; x < world.getSize(); x++) {
            for (int y = 0; y < world.getSize(); y++) {
                world.getPoiMap()[x][y] = PoiFactory.createPoi();
            }
        }
        Position door = getAnyUnoccupiedPosition(world);
        world.getPoiMap()[door.x][door.y] = new Poi(Poi.PoiType.MAGIC_DOOR);
    }

    private void bump() {
        Main.draw("Ouch! You reached the end of the world and it hurt. Go into another direction.");
    }

    public void setPosition(World world, Position pos) {
        world.setPosX(pos.x);
        world.setPosY(pos.y);
    }

    public void setAnyUnoccupiedPosition(World world) {
        setPosition(world, getAnyUnoccupiedPosition(world));
    }

    public Poi getPoi(World world, int posX, int posY) {
        return world.getPoiMap()[posX][posY];
    }

}
