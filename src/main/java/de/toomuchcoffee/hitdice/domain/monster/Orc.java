package de.toomuchcoffee.hitdice.domain.monster;


import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.item.HandWeapon.MACE;

public class Orc extends Monster {
    public Orc() {
        super("Orc", 2, 0, 2, new WeaponAttack(MACE));
    }
}
