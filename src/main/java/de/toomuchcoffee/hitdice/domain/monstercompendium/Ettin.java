package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction.WeaponAttack;

import static de.toomuchcoffee.hitdice.service.Dice.D6;

public class Ettin extends Monster {
    public Ettin() {
        super("Ettin", 4, -1, new CustomWeapon("big club", 1, D6, 2), 3, new WeaponAttack());
    }
}
