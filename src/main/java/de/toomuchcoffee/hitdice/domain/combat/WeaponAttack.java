package de.toomuchcoffee.hitdice.domain.combat;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static java.lang.Math.max;

@RequiredArgsConstructor
public class WeaponAttack implements CombatAction {
    private final Weapon weapon;

    public Optional<String> execute(Combatant attacker, Combatant defender) {
        if (condition(attacker, defender)) {
            return Optional.of(onSuccess(attacker, defender));
        }
        return Optional.empty();
    }

    private boolean condition(Combatant attacker, Combatant defender) {
        int attackScore = max(1, attacker.getAttack() - defender.getDefense());
        return D20.check(attackScore);
    }

    private String onSuccess(Combatant attacker, Combatant defender) {
        int damage = max(1, weapon.getDamage().get()
                + attacker.getDamageBonus()
                - defender.getArmorClass());
        defender.reduceHealth(damage);
        return String.format("%s hit %s with their %s for %d points of damage.", attacker.getName(), defender.getName(), weapon.getName(), damage);
    }
}
