package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.service.CombatService.CombatAction.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D6;

public class Ettin extends Monster {
    public Ettin() {
        super("Ettin", 4, -1, new CustomWeapon("big club", 2, D6, 2), 3, new WeaponAttack());
    }
}
