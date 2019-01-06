package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static de.toomuchcoffee.hitdice.domain.Armor.NONE;
import static de.toomuchcoffee.hitdice.domain.Weapon.FISTS;

@Getter
@Setter
public class Hero extends Combatant {
    private UUID id;

    private int experience;

    private int level = 1;

    public Hero(int strength, int dexterity, int stamina) {
        this.strength = new Attribute(strength);
        this.dexterity = new Attribute(dexterity);
        this.stamina = new Attribute(stamina);
        this.currentStamina = stamina;
    }

    public void increaseExperience(int experience) {
        this.experience += experience;
    }

    @Override
    public Weapon getWeapon() {
        return weapon == null ? FISTS : super.getWeapon();
    }

    @Override
    public Armor getArmor() {
        return armor == null ? NONE : super.getArmor();
    }

    @Override
    public boolean isAlive() {
        return getStrength().getValue() > 0 && super.isAlive();
    }
}
