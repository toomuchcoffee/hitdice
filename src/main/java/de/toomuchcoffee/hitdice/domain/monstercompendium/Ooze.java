package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D6;

public class Ooze extends Monster {
    public Ooze() {
        super("Ooze",
                5,
                -1,
                new CustomWeapon("acid", 1, D6, 0),
                1,
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(5);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        hero.getStamina().decrease();
                        return "The ooze's acid harms you in such a bad way, that you lose one point of stamina!";
                    }
                });
    }
}
