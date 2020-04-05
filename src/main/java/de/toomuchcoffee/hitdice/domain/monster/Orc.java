package de.toomuchcoffee.hitdice.domain.monster;

import static de.toomuchcoffee.hitdice.domain.item.HandWeapon.MACE;

public class Orc extends AbstractMonster {
    public Orc() {
        super("Orc", 2, 0, MACE, 2);
    }
}
