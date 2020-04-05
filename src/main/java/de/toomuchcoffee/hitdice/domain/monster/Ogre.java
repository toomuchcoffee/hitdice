package de.toomuchcoffee.hitdice.domain.monster;

import static de.toomuchcoffee.hitdice.domain.Dice.D6;

public class Ogre extends Monster {
    public Ogre() {
        super("Ogre", 3, -1, new CustomWeapon("big club", 2, D6, 0), 2);
    }
}
