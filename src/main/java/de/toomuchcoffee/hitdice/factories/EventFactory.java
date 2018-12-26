package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.domain.Event;
import de.toomuchcoffee.hitdice.domain.EventType;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;

public class EventFactory {

    public static Event createPoi() {
        int d = D20.roll();
        if (d > 17) {
            return new Event(EventType.POTION, D6.roll(2));
        } else if (d > 14) {
            return new Event(EventType.TREASURE, TreasureFactory.createTreasure());
        } else if (d > 11) {
            return new Event(EventType.MONSTER, MonsterFactory.createMonster());
        } else {
            return new Event(EventType.EMPTY);
        }
    }

}
