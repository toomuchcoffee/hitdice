package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import java.util.List;

import static java.lang.Math.min;

public interface Combatant {
    String getName();

    Attribute getStrength();
    Attribute getDexterity();
    Attribute getStamina();

    int getHealth();
    void setHealth(int health);

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
