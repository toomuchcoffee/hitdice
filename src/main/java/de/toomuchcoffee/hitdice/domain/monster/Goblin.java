package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.combat.Weapon.SHORTSWORD;


public class Goblin extends Monster {
    public Goblin() {
        super("Goblin", 1, 0, 1, new WeaponAttack(SHORTSWORD));
    }
}
