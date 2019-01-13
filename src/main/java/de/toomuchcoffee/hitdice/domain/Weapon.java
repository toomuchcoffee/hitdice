package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.service.DiceService.Dice;

public interface Weapon {
    String getName();
    int getDiceNumber();
    Dice getDice();
    int getBonus();
}
