package de.toomuchcoffee.hitdice.db;

import java.util.Map;

public class ItemTestData {
    public static Item createItem(String displayName,
                                  int ordinal,
                                  boolean metallic,
                                  ItemType type,
                                  Map<String, Object> properties) {
        Item item = new Item();
        item.setDisplayName(displayName);
        item.setOrdinal(ordinal);
        item.setMetallic(metallic);
        item.setItemType(type);
        item.setProperties(properties);
        return item;
    }
}