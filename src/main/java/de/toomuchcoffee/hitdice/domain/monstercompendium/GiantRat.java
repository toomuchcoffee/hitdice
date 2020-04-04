package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;

import static de.toomuchcoffee.hitdice.service.Dice.D3;

public class GiantRat extends Monster {
    public GiantRat() {
        super("Giant Rat", 0, 4, new CustomWeapon("teeth", 1, D3, 0), 0);
    }
}
