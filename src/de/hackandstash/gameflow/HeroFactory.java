package de.hackandstash.gameflow;

import de.hackandstash.serializable.Game;
import de.hackandstash.serializable.Hero;
import de.hackandstash.utilities.Dice;

import static de.hackandstash.Main.*;
import static java.lang.System.lineSeparator;

public class HeroFactory {

    public static Hero create() {
        Hero hero;
        boolean keepHero;
        do {
            hero = new Hero(Dice.D6.roll(3), Dice.D6.roll(3), Dice.D6.roll(3));
            draw("Strength: %d" + lineSeparator() + "Dexterity: %d" + lineSeparator() + "Stamina: %d",
                    hero.getStrength(), hero.getDexterity(), hero.getStamina());
            keepHero = confirm("Keep hero?");
        } while (!keepHero);

        String heroName = dialog("Fantastic! Now give your new hero a name: ");
        hero.setName(heroName);
        draw("Hello %s! That sounds very heroic!" + lineSeparator() +
                        "Only equipped with a dagger you step out into the dark and start your adventure!",
                        hero.getName());
        hero.setWeapon(TreasureFactory.DAGGER);
        return hero;
    }

    public static void gainExperience(Hero hero, int experience) {
        hero.increaseExperience(experience);
        draw("You gained %d experience points.", experience);
        if (hero.getExperience() >= experienceNeededForNextLevel(hero)) {
            hero.setLevel(hero.getLevel() + 1);
            draw("Yohoo, you gained a new experience level!" + lineSeparator() +
                    "You can add one point to one of your attribute scores." + lineSeparator() +
                    "Strength and dexterity can be only increased to a maximum of 18.");
            Game.getInstance().printAttributes();
            increaseAttribute(hero);
        }
    }

    private static void increaseAttribute(Hero hero) {
        boolean valid = false;
        while (!valid) {
            String opt = dialog("Choose the attribute you want to increase: strength (s), dexterity (d), stamina (a): ");
            if ("s".equalsIgnoreCase(opt)) {
                valid = hero.getStrength() < 18;
                if (valid) {
                    hero.setStrength(hero.getStrength()+1);
                }
            } else if ("d".equalsIgnoreCase(opt)) {
                valid = hero.getDexterity() < 18;
                if (valid) {
                    hero.setDexterity(hero.getDexterity() + 1);
                }
            } else if ("a".equalsIgnoreCase(opt)) {
                valid = true;
                hero.setStamina(hero.getStamina()+1);
            }
            if (!valid) {
                draw("Invalid increasement. Choose another attribute.");
            }
        }

    }

    private static int experienceNeededForNextLevel(Hero hero) {
        int xp = 0;
        for (int i=0; i<=hero.getLevel(); i++) {
            xp += i * 100;
        }
        return xp;
    }
}
