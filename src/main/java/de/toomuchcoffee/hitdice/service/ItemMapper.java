package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.event.factory.ArmorFactory;
import de.toomuchcoffee.hitdice.domain.event.factory.PotionFactory;
import de.toomuchcoffee.hitdice.domain.event.factory.ShieldFactory;
import de.toomuchcoffee.hitdice.domain.event.factory.WeaponFactory;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

@Component
public class ItemMapper {
    public Item fromDb(de.toomuchcoffee.hitdice.db.Item dbItem) {
        String s = dbItem.getName();

        if (stream(WeaponFactory.values()).map(WeaponFactory::name).anyMatch(e -> e.equals(s))) {
            return WeaponFactory.valueOf(s).createEvent().getObject();
        }
        if (stream(ArmorFactory.values()).map(ArmorFactory::name).anyMatch(e -> e.equals(s))) {
            return ArmorFactory.valueOf(s).createEvent().getObject();
        }
        if (stream(ShieldFactory.values()).map(ShieldFactory::name).anyMatch(e -> e.equals(s))) {
            return ShieldFactory.valueOf(s).createEvent().getObject();
        }
        if (stream(PotionFactory.values()).map(PotionFactory::name).anyMatch(e -> e.equals(s))) {
            return PotionFactory.valueOf(s).createEvent().getObject();
        }
        throw new IllegalStateException("Value doesn't match any registered enum: " + s);
    }

    public de.toomuchcoffee.hitdice.db.Item toDb(Item item) {
        de.toomuchcoffee.hitdice.db.Item dbItem = new de.toomuchcoffee.hitdice.db.Item();
        dbItem.setName(item.getFactory().name()); // FIXME
        return dbItem;
    }

}
