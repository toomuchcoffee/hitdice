package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;

import static de.toomuchcoffee.hitdice.domain.HandWeapon.LONGSWORD;

public class Skeleton extends Monster {
    public Skeleton() {
        super("Skeleton", 1, -2, LONGSWORD, 3);
    }
}
