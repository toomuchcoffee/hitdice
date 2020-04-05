package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D6;

public class Lich extends Monster {
    public Lich() {
        super("Lich",
                7,
                0,
                new CustomWeapon("touch", 1, D6, 0),
                0,
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
