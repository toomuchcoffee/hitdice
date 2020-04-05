package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import java.util.List;

import static java.lang.Math.min;

public interface Combatant {
    String getName();

    int getLevel();
    int getAttack();
    int getDefense();
    int getDamageBonus();

    Health getHealth();

    Weapon getWeapon();

    List<CombatAction> getCombatActions();

    int getArmorClass();

    default void reduceHealth(int damage) {
        int effectiveDamage = min(damage, getHealth().getValue());
        getHealth().decrease(effectiveDamage);
    }

    default boolean isDefeated() {
        return getHealth().getValue() <= 0;
    }
}
