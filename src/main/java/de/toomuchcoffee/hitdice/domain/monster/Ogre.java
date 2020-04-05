package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D6;

public class Ogre extends Monster {
    public Ogre() {
        super("Ogre",
                3,
                -1,
                2,
                new WeaponAttack(new CustomWeapon("big club", 2, D6, 0))
                );
    }
}
