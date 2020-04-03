package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.EventType.POTION;
import static java.lang.Math.min;

@RequiredArgsConstructor
@Getter
public class Potion implements Event {
    private final int power;
    private final Type type;

    public enum Type {
        HEALING, STRENGTH, STAMINA
    }

    public EventType getEventType() {
        return POTION;
    }

    public void applyOn(Hero hero) {
        switch (type) {
            case HEALING:
                hero.setHealth(min(hero.getHealth() + power, hero.getMaxHealth()));
                break;
            case STRENGTH:
                hero.getStrength().increase(power);
                break;
            case STAMINA:
                hero.getStamina().increase(power);
                break;
        }
    }
}
