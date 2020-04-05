package de.toomuchcoffee.hitdice.domain.monster;

import static de.toomuchcoffee.hitdice.domain.item.HandWeapon.SHORTSWORD;

public class Goblin extends Monster {
    public Goblin() {
        super("Goblin", 1, 0, SHORTSWORD, 1);
    }
}
