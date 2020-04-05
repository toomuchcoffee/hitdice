package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.*;

public class Demogorgon extends Monster {
    public Demogorgon() {
        super("Demogorgon",
                7,
                2,
                0,
                new WeaponAttack(new CustomWeapon("bite", () -> D8.roll(2))),
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
