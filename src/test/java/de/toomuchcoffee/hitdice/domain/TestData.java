package de.toomuchcoffee.hitdice.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class TestData {
    public static Hero getHero() {
        Hero hero = new Hero();
        ReflectionTestUtils.setField(hero, "strength", new Attribute(10));
        ReflectionTestUtils.setField(hero, "dexterity", new Attribute(11));
        ReflectionTestUtils.setField(hero, "stamina", new Attribute(12));
        ReflectionTestUtils.setField(hero, "health", 12);
        ReflectionTestUtils.setField(hero, "maxHealth", 12);
        return hero;
    }
}
