package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.combat.Weapon.LONGSWORD;


public class Skeleton extends Monster {
    public Skeleton() {
        super("Skeleton", 1, -2, 3, new WeaponAttack(LONGSWORD));
    }
}
