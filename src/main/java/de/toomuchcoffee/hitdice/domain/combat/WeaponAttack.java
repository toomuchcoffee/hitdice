package de.toomuchcoffee.hitdice.domain.combat;

import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static java.lang.Math.max;

@RequiredArgsConstructor
public class WeaponAttack implements CombatAction {
    private final GenericWeapon weapon;

    public boolean condition(Combatant attacker, Combatant defender) {
        int attackScore = max(1, attacker.getAttack() - defender.getDefense());
        return D20.check(attackScore);
    }

    @Override
    public String onSuccess(Combatant attacker, Combatant defender) {
        int damage = max(1, weapon.getDamage().get()
                + attacker.getDamageBonus()
                - defender.getArmorClass());
        defender.reduceHealth(damage);
        return String.format("%s hit %s with their %s for %d points of damage.", attacker.getName(), defender.getName(), weapon.getName(), damage);
    }
}
