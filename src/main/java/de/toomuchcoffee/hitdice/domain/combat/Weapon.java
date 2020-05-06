package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.Dice;

public interface Weapon {
    String getDisplayName();
    Dice getDamage();
}
