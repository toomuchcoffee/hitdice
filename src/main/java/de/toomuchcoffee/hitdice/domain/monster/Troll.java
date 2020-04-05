package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.domain.Dice.D10;
import static de.toomuchcoffee.hitdice.domain.Dice.D3;

public class Troll extends AbstractMonster {
    public Troll() {
        super("Troll",
                4,
                -1,
                new CustomWeapon("claws", 1, D10, 0),
                3,
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return attacker.getHealth().isInjured();
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        int regeneration = D3.roll();
                        attacker.getHealth().increase(regeneration);
                        return String.format("Oh no! The troll regenerated %d points of stamina!", regeneration);
                    }
                });
    }
}
