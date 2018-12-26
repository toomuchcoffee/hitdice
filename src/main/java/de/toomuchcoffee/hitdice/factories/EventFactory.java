package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.domain.Event;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;
import static de.toomuchcoffee.hitdice.domain.Event.EMPTY_EVENT;
import static de.toomuchcoffee.hitdice.domain.EventType.*;

public class EventFactory {

    public static Event create() {
        int d = D20.roll();
        if (d > 17) {
            return new Event(POTION, D6.roll(2));
        } else if (d > 14) {
            return new Event(TREASURE, TreasureFactory.createTreasure());
        } else if (d > 11) {
            return new Event(MONSTER, MonsterFactory.createMonster());
        } else {
            return EMPTY_EVENT;
        }
    }

}
