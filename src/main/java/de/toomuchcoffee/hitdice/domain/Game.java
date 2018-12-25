package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.Main;

import java.io.*;
import java.util.Date;

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


    private Game(Hero hero ) {
        this.hero = hero;
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
