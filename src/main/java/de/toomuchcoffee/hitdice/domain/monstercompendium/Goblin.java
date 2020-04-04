package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;

import static de.toomuchcoffee.hitdice.domain.HandWeapon.SHORTSWORD;

public class Goblin extends Monster {
    public Goblin() {
        super("Goblin", 1, 0, SHORTSWORD, 1);
    }
}
