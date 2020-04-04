package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D4;

public class Vampire extends Monster {
    public Vampire() {
        super("Vampire",
                5,
                2,
                new Monster.NaturalWeapon("bite", 2, D4, 0),
                0,
                200,
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(5);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        hero.getStrength().decrease();
                        return "Don't you just hate vampires? This fella just sucked away one point of strength from you!";
                    }
                });
    }
}
