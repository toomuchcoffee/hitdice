package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;

import static de.toomuchcoffee.hitdice.domain.HandWeapon.MACE;

public class Orc extends Monster {
    public Orc() {
        super("Orc", 2, 0, MACE, 2);
    }
}
