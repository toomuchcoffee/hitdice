package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;

import static de.toomuchcoffee.hitdice.service.Dice.D6;

public class Ogre extends Monster {
    public Ogre() {
        super("Ogre", 3, -1, new CustomWeapon("big club", 2, D6, 0), 2);
    }
}
