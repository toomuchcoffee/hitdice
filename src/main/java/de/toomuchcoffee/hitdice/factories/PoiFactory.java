package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.domain.Event;

import static de.toomuchcoffee.hitdice.domain.Event.EventType;
import static de.toomuchcoffee.hitdice.utilities.Dice.D20;
import static de.toomuchcoffee.hitdice.utilities.Dice.D6;

public class PoiFactory {

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
