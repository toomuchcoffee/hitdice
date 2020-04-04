package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D8;

public class Dragon extends Monster {
    public Dragon() {
        super("Dragon",
                8,
                0,
                new Monster.NaturalWeapon("claws", 1, D8, 0),
                5,
                400,
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return D20.check(5);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        int damage = D8.roll(2);
                        defender.reduceHealth(damage);
                        return String.format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage);
                    }
                });
    }
}
