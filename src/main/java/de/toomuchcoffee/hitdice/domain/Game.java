package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.Main;
import de.toomuchcoffee.hitdice.factories.HeroFactory;

import java.io.*;
import java.util.Date;
import java.util.Random;

import static java.lang.System.lineSeparator;

public class Game implements Serializable {
    private static final long serialVersionUID = -8649070405517008038L;

    private Hero hero;
    private Date timestamp;
    private World world;

    public static String FILEPATH = ".hitdice.ser";

    private static Game instance;

    public static Game createGame(Hero hero) {
        instance = new Game(hero);
        return instance;
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (hero.isAlive()) {
                Main.draw("Saving current game...");
                try {
                    Game.saveGameState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        while (hero.isAlive()) {
            printStatus();
            nextTurn();
        }
    }

    public void nextTurn() {
        String option = Main.dialog("Move North (n), West (w), East(e), South (s), show inventory (i), quit (q): ");
        if ("n".equalsIgnoreCase(option)) {
            world.goNorth();
            checkIncident();
        } else if("e".equalsIgnoreCase(option)) {
            world.goEast();
            checkIncident();
        } else if ("w".equalsIgnoreCase(option)) {
            world.goWest();
            checkIncident();
        } else if ("s".equalsIgnoreCase(option)) {
            world.goSouth();
            checkIncident();
        } else if ("i".equalsIgnoreCase(option)) {
            printInventory();
        } else if ("m".equalsIgnoreCase(option)) {
            printStatus();
        } else if ("q".equalsIgnoreCase(option)) {
            Main.draw("Bye!");
            System.exit(0);
        } else {
            Main.draw("What else do you want to do? Fly? Think again.");
        }
    }

    private void checkIncident() {
        Poi poi = world.getPoi();
        switch (poi.getType()) {
            case MONSTER: {
                Combat combat = new Combat(hero, (Monster) poi.getObject());
                if (combat.fight()) {
                    HeroFactory.gainExperience(hero, combat.getExperienceValue());
                    world.markAsVisited();
                } else {
                    world.setAnyUnoccupiedPosition();
                }
                break;
            }
            case POTION: {
                int recovery = (Integer) poi.getObject();
                Main.draw("You found a healing potion and recover %d of stamina.", recovery);
                hero.recoverStaminaBy(recovery);
                world.markAsVisited();
                break;
            }
            case TREASURE: {
                Treasure treasure = (Treasure) poi.getObject();
                Main.draw("You found a %s", treasure.getName());
                printInventory();
                if (Main.confirm("Do you want to take the " + treasure.getName() + "?")) {
                    hero.stash(treasure);
                }
                world.markAsVisited();
                break;
            }
            case EXPLORED: {
                Main.draw("You've been here before. Turn the page and move on.");
                world.markAsVisited();
                break;
            }
            case MAGIC_DOOR: {
                Main.draw("You see a strange mystical sign that someone has drawn on the floor" + lineSeparator() +
                        "As you step on it suddenly everything around you dissolves into strange shapes and colors" + lineSeparator() +
                        " only to reshape into a complete new surrounding!" + lineSeparator() +
                        "You entered yet another world. Oh no!");
                enterNewWorld();
                break;
            }
            case EMPTY:{
            }
            default: {
                Main.draw("Pretty boring area here. Let's move on.");
                world.markAsVisited();
            }
        }
    }


    private Game(Hero hero ) {
        this.hero = hero;
        enterNewWorld();
    }

    public void enterNewWorld() {
        world = new World(new Random().nextInt(hero.getLevel()+4)+5);
        World.Position start = world.getAnyUnoccupiedPosition();
        world.setPosition(start);
        world.markAsVisited();
    }

    public static Game recoverGameState() throws Exception {
        FileInputStream fis = new FileInputStream(FILEPATH);
        ObjectInputStream ois = new ObjectInputStream(fis);
        instance = (Game) ois.readObject();
        ois.close();
        return instance;
    }

    public static void saveGameState() throws Exception {
        getInstance().setTimestamp();
        FileOutputStream fos = new FileOutputStream(FILEPATH);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(instance);
        oos.close();
    }

    public static Game getInstance() {
        return instance;
    }

    public void printHealth() {
        Main.draw("Stamina: %d/%d", hero.getCurrentStamina(), hero.getStamina());
    }

    public void printExperience() {
        Main.draw("Experience: %d. (Level %d).", hero.getExperience(), hero.getLevel());
    }

    public void printMap() {
        Main.draw(world.getMap());
    }

    public void printAttributes() {
        Main.draw("%s's attributes are: strength (%d), dexterity (%d), stamina (%d)",
                hero.getName(), hero.getStrength(), hero.getDexterity(), hero.getStamina());
    }

    public void printInventory() {
        Main.draw("%s's inventory: ", hero.getName());
        for (Treasure t : hero.getInventory()) {
            Main.draw("- %s", t.getName());
        }
    }

    public void printStatus() {
        printHealth();
        printExperience();
        printMap();
    }

    public void setTimestamp() {
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
