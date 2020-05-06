package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.db.ItemType;
import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import de.toomuchcoffee.hitdice.domain.equipment.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ItemMapper {
    public Item fromDb(de.toomuchcoffee.hitdice.db.Item dbItem) {
        ItemType type = dbItem.getItemType();
        switch (type){
            case ARMOR:
                return Armor.builder()
                        .displayName(dbItem.getDisplayName())
                        .metallic(dbItem.isMetallic())
                        .ordinal(dbItem.getOrdinal())
                        .protection((Integer) dbItem.getProperties().get("protection"))
                        .build();
            case WEAPON:
                return Weapon.builder()
                        .displayName(dbItem.getDisplayName())
                        .metallic(dbItem.isMetallic())
                        .ordinal(dbItem.getOrdinal())
                        .damage(Dice.deserialize((String) dbItem.getProperties().get("damage")))
                        .build();
            case SHIELD:
                return Shield.builder()
                        .displayName(dbItem.getDisplayName())
                        .metallic(dbItem.isMetallic())
                        .ordinal(dbItem.getOrdinal())
                        .defense((Integer) dbItem.getProperties().get("defense"))
                        .build();
            case POTION:
                return Potion.builder()
                        .displayName(dbItem.getDisplayName())
                        .metallic(dbItem.isMetallic())
                        .ordinal(dbItem.getOrdinal())
                        .potency(Dice.deserialize((String) dbItem.getProperties().get("potency")))
                        .type(AttributeType.valueOf((String) dbItem.getProperties().get("type")))
                        .build();
            default:
                throw new IllegalArgumentException("Unsupported item type: " + type);
        }
    }

    public de.toomuchcoffee.hitdice.db.Item toDb(Item item) {
        de.toomuchcoffee.hitdice.db.Item dbItem = new de.toomuchcoffee.hitdice.db.Item();
        ItemType type;
        Map<String, Object> properties = new HashMap<>();
        if (item instanceof Armor) {
            type = ItemType.ARMOR;
            properties.put("protection", ((Armor) item).getProtection());
        } else if (item instanceof Weapon) {
            type = ItemType.WEAPON;
            properties.put("damage", ((Weapon) item).getDamage().serialize());
        } else if (item instanceof Shield) {
            type = ItemType.SHIELD;
            properties.put("defense", ((Shield) item).getDefense());
        } else if (item instanceof Potion) {
            type = ItemType.POTION;
            properties.put("potency", ((Potion) item).getPotency().serialize());
            properties.put("type", ((Potion) item).getType().name());
        } else {
            throw new IllegalArgumentException("Unsupported item class: " + item.getClass().getName());
        }
        dbItem.setItemType(type);
        dbItem.setProperties(properties);
        dbItem.setDisplayName(item.getDisplayName());
        dbItem.setMetallic(item.isMetallic());
        dbItem.setOrdinal(item.getOrdinal());
        return dbItem;
    }

}
