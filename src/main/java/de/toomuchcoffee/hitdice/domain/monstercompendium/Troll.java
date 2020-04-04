package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static de.toomuchcoffee.hitdice.service.Dice.D10;
import static de.toomuchcoffee.hitdice.service.Dice.D3;

public class Troll extends Monster {
    public Troll() {
        super("Troll",
                3,
                -1,
                new Monster.NaturalWeapon("claws", 1, D10, 0),
                3,
                100,
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return attacker.getHealth() > 0 && attacker.getHealth() < attacker.getMaxHealth();
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        int regeneration = D3.roll();
                        attacker.setHealth(Math.min(attacker.getHealth() + regeneration, attacker.getMaxHealth()));
                        return String.format("Oh no! The troll regenerated %d points of stamina!", regeneration);
                    }
                });
    }
}
