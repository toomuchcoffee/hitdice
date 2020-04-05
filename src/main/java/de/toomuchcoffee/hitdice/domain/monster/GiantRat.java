package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D3;

public class GiantRat extends Monster {
    public GiantRat() {
        super("Giant Rat",
                0,
                4,
                0,
                new WeaponAttack(new CustomWeapon("teeth", 1, D3, 0))
        );
    }
}
