package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D8;

public class Dragon extends Monster {
    public Dragon() {
        super("Dragon",
                8,
                0,
                5,
                new WeaponAttack(new CustomWeapon("claws", 1, D8, 0)),
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return D20.check(5);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        int damage = D8.roll(2);
                        defender.reduceHealth(damage);
                        return String.format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage);
                    }
                });
    }
}
