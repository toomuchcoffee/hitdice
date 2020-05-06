package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.equipment.Weapon;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static java.lang.Math.max;

@RequiredArgsConstructor(staticName = "with")
public class Attack implements CombatAction {
    private final Weapon weapon;

    public String execute(Combatant attacker, Combatant defender) {
        return condition(attacker, defender) ? onSuccess(attacker, defender) : null;
    }

    private boolean condition(Combatant attacker, Combatant defender) {
        int attackScore = max(1, attacker.getAttack() - defender.getDefense());
        return D20.check(attackScore);
    }

    private String onSuccess(Combatant attacker, Combatant defender) {
        int damage = max(1, weapon.getDamage().roll()
                + attacker.getDamageBonus()
                - defender.getArmorClass());
        defender.reduceHealth(damage);
        return String.format("%s hit %s with their %s for %d points of damage.", attacker.getName(), defender.getName(), weapon.getDisplayName(), damage);
    }
}
