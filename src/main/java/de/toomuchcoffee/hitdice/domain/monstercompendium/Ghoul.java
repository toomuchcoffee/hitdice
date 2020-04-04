package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D4;

public class Ghoul extends Monster {
    public Ghoul() {
        super("Ghoul",
                2,
                -1,
                new CustomWeapon("claws", 1, D4, 0),
                0,
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(5);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        hero.getDexterity().decrease();
                        return "The ghoul's paralyzing touch makes you lose one point of dexterity!";
                    }
                });
    }
}
