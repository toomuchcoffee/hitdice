package de.toomuchcoffee.hitdice.domain.monster;

import static de.toomuchcoffee.hitdice.domain.Dice.D3;

public class GiantRat extends AbstractMonster {
    public GiantRat() {
        super("Giant Rat", 0, 4, new CustomWeapon("teeth", 1, D3, 0), 0);
    }
}
