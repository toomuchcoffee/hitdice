package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public class Armor implements Treasure {
    public static final Armor LEATHER = new Armor("leather armor", 2, false);
    public static final Armor CHAIN = new Armor("chain mail", 3, true);
    public static final Armor PLATE = new Armor("plate armor", 4, true);

    private final String name;
    private final int protection;
    private final boolean metallic;

    public String getDisplayName() {
        return format("%s (ac: %d)", name, protection);
    }
}
