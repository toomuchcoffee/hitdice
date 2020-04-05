package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D4;

public class MindFlayer extends Monster {
    public MindFlayer() {
        super("Mind Flayer",
                6,
                1,
                new CustomWeapon("claws", 2, D4, 0),
                0,
                new WeaponAttack() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return super.condition(defender, defender);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        return super.onSuccess(defender, defender);
                    }
                });
    }
}
