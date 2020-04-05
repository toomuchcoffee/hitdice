package de.toomuchcoffee.hitdice.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class TestData {
    public static Hero getHero() {
        Hero hero = new Hero();
        hero.initializeWithPresets(10, 11, 12);
        ReflectionTestUtils.setField(hero.getHealth(), "value", 12);
        ReflectionTestUtils.setField(hero.getHealth(), "maxValue", 12);
        ReflectionTestUtils.setField(hero, "level", 1);
        return hero;
    }
}
