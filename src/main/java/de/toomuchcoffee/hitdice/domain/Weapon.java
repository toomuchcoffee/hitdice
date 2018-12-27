package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Weapon implements Treasure {
    private final String name;
    private final int diceNumber;
    private final Dice dice;
    private final int bonus;
    private final boolean metallic;

    public int damage() {
        return dice.roll(diceNumber) + bonus;
    };

    public void specialDamage(Combatant defender) {

    }
}
