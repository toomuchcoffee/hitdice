package de.toomuchcoffee.hitdice.domain;

public class TestData {
    public static Hero getHero() {
        Hero hero = new Hero();
        hero.initializeWithPresets(10, 11, 12);
        hero.setHealth(12);
        hero.setMaxHealth(12);
        return hero;
    }
}
