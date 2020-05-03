package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.equipment.Armor;
import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

@Component
public class ItemMapper {
    public Item fromDb(de.toomuchcoffee.hitdice.db.Item dbItem) {
        String s = dbItem.getName();

        if (stream(HandWeapon.values()).map(HandWeapon::name).anyMatch(e -> e.equals(s))) {
            return HandWeapon.valueOf(s);
        }
        if (stream(Armor.values()).map(Armor::name).anyMatch(e -> e.equals(s))) {
            return Armor.valueOf(s);
        }
        if (stream(Potion.values()).map(Potion::name).anyMatch(e -> e.equals(s))) {
            return Potion.valueOf(s);
        }
        throw new IllegalStateException("Value doesn't match any registered enum: " + s);
    }

    public de.toomuchcoffee.hitdice.db.Item toDb(Item item) {
        de.toomuchcoffee.hitdice.db.Item dbItem = new de.toomuchcoffee.hitdice.db.Item();
        dbItem.setName(item.getName());
        return dbItem;
    }

}
