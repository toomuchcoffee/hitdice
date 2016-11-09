package de.toomuchcoffee.hackandstash.factories;

import de.toomuchcoffee.hackandstash.domain.Poi;

import static de.toomuchcoffee.hackandstash.domain.Poi.PoiType;
import static de.toomuchcoffee.hackandstash.utilities.Dice.*;

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
