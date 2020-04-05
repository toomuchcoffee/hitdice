package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;

public class Ghoul extends Monster {
    public Ghoul() {
        super("Ghoul",
                2,
                -1,
                0,
                new WeaponAttack(new CustomWeapon("claws", 1, D4, 0)),
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(5);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        hero.getDexterity().decrease(1);
                        return "The ghoul's paralyzing touch makes you lose one point of dexterity!";
                    }
                });
    }
}
