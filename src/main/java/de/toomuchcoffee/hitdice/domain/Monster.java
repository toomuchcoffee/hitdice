package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;

import static java.util.Arrays.asList;

public class Monster extends Combatant {

    private int value;

    public Monster(String name, int strength, int dexterity, int stamina, Weapon weapon, int value, CombatAction... additionalActions) {
        this.name = name;
        this.strength = new Attribute(strength);
        this.dexterity = new Attribute(dexterity);
        this.stamina = new Attribute(stamina);
        this.health = stamina;
        this.weapon = weapon;
        this.value = value;
        this.combatActions.addAll(asList(additionalActions));
    }

    public int getValue() {
        return value;
    }

}
