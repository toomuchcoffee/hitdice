package de.toomuchcoffee.hitdice.domain;

public class Monster extends Combatant {

    private int value;

    public Monster(String name, int strength, int dexterity, int stamina, Weapon weapon, int value) {
        this.name = name;
        this.strength = new Attribute(strength);
        this.dexterity = new Attribute(dexterity);
        this.stamina = new Attribute(stamina);
        this.currentStamina = stamina;
        this.weapon = weapon;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
