package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;

public class Lich extends Monster {
    public Lich() {
        super("Lich",
                7,
                0,
                0,
                new WeaponAttack(new CustomWeapon("touch", D6::roll)),
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(4);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        hero.getStrength().decrease(1);
                        hero.getDexterity().decrease(1);
                        hero.getStamina().decrease(1);
                        return "The eternal coldness of the Lich's touch weakens you. You lose 1 point on each attribute score!";
                    }
                });
    }
}
