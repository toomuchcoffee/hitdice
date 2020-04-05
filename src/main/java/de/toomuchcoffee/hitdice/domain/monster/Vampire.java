package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;

public class Vampire extends Monster {
    public Vampire() {
        super("Vampire",
                5,
                2,
                0,
                new WeaponAttack(new CustomWeapon("bite", 2, D4, 0)),
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(5);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        hero.getStrength().decrease(1);
                        return "Don't you just hate vampires? This fella just sucked away one point of strength from you!";
                    }
                });
    }
}
