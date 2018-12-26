package de.toomuchcoffee.hitdice.domain;

public class Weapon extends Treasure {
    private int diceNumber;
    private Dice dice;
    private int bonus;


    public Weapon(String name, int diceNumber, Dice dice, int bonus, boolean metallic) {
        this.name = name;
        this.diceNumber = diceNumber;
        this.dice = dice;
        this.bonus = bonus;
        this.metallic = metallic;
    }

    public int damage() {
        return dice.roll(diceNumber) + bonus;
    };

    public void specialDamage(Combatant defender) {

    }

}
