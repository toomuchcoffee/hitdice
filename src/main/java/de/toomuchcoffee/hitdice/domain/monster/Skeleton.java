package de.toomuchcoffee.hitdice.domain.monster;

import static de.toomuchcoffee.hitdice.domain.item.HandWeapon.LONGSWORD;

public class Skeleton extends AbstractMonster {
    public Skeleton() {
        super("Skeleton", 1, -2, LONGSWORD, 3);
    }
}
