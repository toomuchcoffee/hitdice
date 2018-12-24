package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Combatant implements Serializable {
    private static final long serialVersionUID = -7292227750870699859L;

    protected String name;

    protected int strength;
    protected int dexterity;
    protected int stamina;

    protected int currentStamina;

    protected Weapon weapon;
    protected Armor armor;

    public boolean isAlive() {
        return getCurrentStamina() > 0;
    }

    public int getCurrentStamina() {
        return currentStamina;
    }

    public void decreaseCurrentStaminaBy(int decreasement) {
        this.currentStamina -= decreasement;
    }

    public List<Treasure> getInventory() {
        List<Treasure> inventory = new ArrayList<Treasure>();
        if (getWeapon()!=null) {
            inventory.add(getWeapon());
        }
        if (getArmor()!=null) {
            inventory.add(getArmor());
        }
        return inventory;
    }

    public abstract int damage();

    public int getAttributeBonus(int attributeValue) {
        if (attributeValue >= 18) {
            return 3;
        }
        if (attributeValue >= 16) {
            return 2;
        }
        if (attributeValue >= 13) {
            return 1;
        }
        if (attributeValue < 9) {
            return -1;
        }
        return 0;
    }

    public void specialAttack(Combatant defender) {
        if(getWeapon() != null) {
            getWeapon().specialDamage(defender);
        }
    }

    public void specialDefense(Combatant attacker) {

    }
}
