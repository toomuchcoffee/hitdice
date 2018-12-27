package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Hero extends Combatant {
    private UUID id;

    private int experience;

    private int level = 1;

    public Hero(int strength, int dexterity, int stamina) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.stamina = stamina;
        this.currentStamina = stamina;
    }

    public void increaseExperience(int experience) {
        this.experience += experience;
    }

    public int damage() {
        return (getWeapon() != null ? getWeapon().damage() : Dice.D2.roll()) + getAttributeBonus(getStrength());
    }

    @Override
    public boolean isAlive() {
        return getStrength() > 0 && super.isAlive();
    }
}
