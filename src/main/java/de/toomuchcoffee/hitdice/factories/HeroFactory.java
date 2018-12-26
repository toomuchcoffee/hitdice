package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.domain.Hero;

public class HeroFactory {

    public static void gainExperience(Hero hero, int experience) {
        hero.increaseExperience(experience);
        //Main.draw("You gained %d experience points.", experience);
        if (hero.getExperience() >= experienceNeededForNextLevel(hero)) {
            hero.setLevel(hero.getLevel() + 1);
           // Main.draw("Yohoo, you gained a new experience level!" + lineSeparator() +
             //       "You can add one point to one of your attribute scores." + lineSeparator() +
               //     "Strength and dexterity can be only increased to a maximum of 18.");
            increaseAttribute(hero);
        }
    }

    private static void increaseAttribute(Hero hero) {
        boolean valid = false;
        while (!valid) {
            //String opt = Main.dialog("Choose the attribute you want to increase: strength (s), dexterity (d), stamina (a): ");

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
