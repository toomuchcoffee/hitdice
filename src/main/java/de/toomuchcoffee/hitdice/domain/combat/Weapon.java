package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.Dice;

public interface Weapon {
    String getName();
    int getDiceNumber();
    Dice getDice();
    int getBonus();
}
