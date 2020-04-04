package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.service.Dice.*;

public class Demogorgon extends Monster {
    public Demogorgon() {
        super("Demogorgon",
                7,
                2,
                new CustomWeapon("bite", 2, D8, 0),
                0,
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(10);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        int damage = D12.roll();
                        hero.reduceHealth(damage);
                        return String.format("The Demogorgon's tentacles embrace you and sending out electric shocks, causing %d extra points of damage!", damage);
                    }
                });
    }
}
