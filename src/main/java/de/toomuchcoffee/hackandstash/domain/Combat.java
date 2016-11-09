package de.toomuchcoffee.hackandstash.domain;

import de.toomuchcoffee.hackandstash.Main;

import static de.toomuchcoffee.hackandstash.utilities.Dice.D20;

public class Combat {

    private Hero hero;
    private Monster enemy;

    public Combat(Hero hero, Monster enemy) {
        this.hero = hero;
        this.enemy = enemy;
    }

    public boolean fight() {
        Main.draw("%s encounters a %s.", hero.getName(), enemy.getName());
        boolean flee = false;
        while (!flee && hero.isAlive() && enemy.isAlive()) {
            Game.getInstance().printHealth();
            flee = Main.confirm("Do you want to flee?");
            if (flee) {
                Main.draw("You successfully fled from the deadly encounter.");
            } else {
                if (killOpponent(hero, enemy)) {
                    Main.draw("Your enemy is dead!");
                } else if (killOpponent(enemy, hero)) {
                    Main.draw("You are dead!");
                    System.exit(0);
                }
            }
        }
        return !flee;
    }

    private boolean killOpponent(Combatant attacker, Combatant defender) {
        int attackScore = Math.max(1, attacker.getDexterity() - defender.getAttributeBonus(defender.getDexterity()));
        if (D20.roll() <= attackScore) {
            int protection = defender.getArmor() != null ? defender.getArmor().getProtection() : 0;
            int damage = Math.max(0, attacker.damage() - protection);
            defender.decreaseCurrentStaminaBy(damage);
            Main.draw("%s hit %s and caused %d points of damage", attacker.getName(), defender.getName(), damage);
            attacker.specialAttack(defender);
            defender.specialDefense(attacker);
        } else {
            Main.draw("%s failed to hit %s", attacker.getName(), defender.getName());
        }
        if (!defender.isAlive()) {
            Main.draw("The fight is over");
            return true;
        }
        return false;
    }

    public int getExperienceValue() {
        return enemy.getValue();
    }
}
