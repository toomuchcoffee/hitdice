package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.domain.Poi;

import static de.toomuchcoffee.hitdice.domain.Poi.PoiType;
import static de.toomuchcoffee.hitdice.utilities.Dice.D20;
import static de.toomuchcoffee.hitdice.utilities.Dice.D6;

public class PoiFactory {

    public static Poi createPoi() {
        int d = D20.roll();
        if (d > 17) {
            return new Poi(PoiType.POTION, D6.roll(2));
        } else if (d > 14) {
            return new Poi(PoiType.TREASURE, TreasureFactory.createTreasure());
        } else if (d > 11) {
            return new Poi(PoiType.MONSTER, MonsterFactory.createMonster());
        } else {
            return new Poi(PoiType.EMPTY);
        }
    }

}
