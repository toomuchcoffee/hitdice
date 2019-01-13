package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import java.util.List;

import static java.lang.Math.min;

public interface Combatant {
    String getName();

    int getLevel();
    int getAttack();
    int getDefense();
    int getDamageBonus();

    int getHealth();
    void setHealth(int health);

    int getMaxHealth();

    Weapon getWeapon();

    List<CombatAction> getCombatActions();

    int getArmorClass();

    void reduceHealth(int damage);

    boolean isAlive();

    abstract class AbstractCombatant implements Combatant {
        public void reduceHealth(int damage) {
            int effectiveDamage = min(damage, getHealth());
            setHealth(getHealth() - effectiveDamage);
        }
        public boolean isAlive() {
            return getHealth() > 0;
        }
    }

}
