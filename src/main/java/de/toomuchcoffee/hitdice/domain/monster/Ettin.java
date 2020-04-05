package de.toomuchcoffee.hitdice.domain.monster;


import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D6;

public class Ettin extends Monster {
    public Ettin() {
        super("Ettin",
                4,
                -1,
                3,
                new WeaponAttack(new CustomWeapon("big club", () -> D6.roll(2))),
                new WeaponAttack(new CustomWeapon("other big club", () -> D6.roll(2)))
        );
    }
}
