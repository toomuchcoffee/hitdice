package de.hackandstash.serializable;

public class Monster extends Combatant {

    private int value;

    public Monster(String name, int dexterity, int stamina, Weapon weapon, int value) {
        this.name = name;
        this.dexterity = dexterity;
        this.stamina = stamina;
        this.currentStamina = stamina;
        this.weapon = weapon;
        this.value = value;
    }

    public int damage() {
        return getWeapon().damage();
    }

    public int getValue() {
        return value;
    }

}
