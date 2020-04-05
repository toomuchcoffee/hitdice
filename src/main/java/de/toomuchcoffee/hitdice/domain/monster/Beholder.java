package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D10;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;

public class Beholder extends Monster {
    public Beholder() {
        super("Beholder",
                6,
                0,
                0,
                new WeaponAttack(new CustomWeapon("bite", 3, D4, 0)),
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return true;
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        int eyes = D10.roll();
                        hero.reduceHealth(eyes);
                        return String.format("%d of the Beholder's eyes beam right into your soul and reduce your health by %d points!", eyes, eyes);
                    }
                });
    }
}
