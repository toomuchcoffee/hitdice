package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public abstract class Combatant {
    protected String name;

    protected Attribute strength;
    protected Attribute dexterity;
    protected Attribute stamina;

    protected int currentStamina;

    protected Weapon weapon;
    protected Armor armor;

    public boolean isAlive() {
        return getCurrentStamina() > 0;
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

    public Optional<String> specialAttack(Combatant defender) {
        if (getWeapon() != null) {
            return getWeapon().specialDamage(defender);
        }
        return Optional.empty();
    }

    public Optional<String> specialDefense(Combatant attacker) {
        return Optional.empty();
    }
}
