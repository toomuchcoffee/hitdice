package de.hackandstash.gameflow;

import de.hackandstash.Main;
import de.hackandstash.serializable.Combatant;
import de.hackandstash.serializable.Monster;
import de.hackandstash.serializable.Weapon;

import static de.hackandstash.utilities.Dice.*;

public class MonsterFactory {
    public static Monster createMonster() {
        int result = D100.roll();
        if (result < 30) {
            return new Monster("Rat", 14, 2, new Weapon("teeth", 1, D3, 0, false), 5);
        } else if (result < 55) {
            return new Monster("Goblin", D6.roll(2), D6.roll(2), TreasureFactory.SHORTSWORD, 15);
        } else if (result < 75) {
            return new Monster("Orc", D6.roll(3)-1, D6.roll(3)+1, TreasureFactory.LONGSWORD, 25);
        } else if (result < 90) {
            return new Monster("Rust monster", D6.roll(3), D6.roll(4), new Weapon("tail", 1, D4, 0, false), 50){
                @Override
                public void specialAttack(Combatant hero) {
                    if (D20.roll() < 9) {
                        if (hero.getWeapon() != null && hero.getWeapon().isMetallic()) {
                            hero.setWeapon(null);
                            Main.draw("Oh no! The $%&§ rust monster hit your weapon and it crumbles to rust.");
                        } else if (hero.getArmor() != null && hero.getArmor().isMetallic()) {
                            hero.setArmor(null);
                            Main.draw("Friggin rust monster! It hit your armor and it crumbles to rust.");
                        }
                    }
                }
            };
        } else if (result < 97) {
            return new Monster("Troll", D6.roll(2), D6.roll(4), new Weapon("claws", 2, D6, 0, false) , 100){
                @Override
                public void specialDefense(Combatant hero) {
                    if (this.getCurrentStamina() < this.getStamina()) {
                        int regeneration = D3.roll();
                        this.setStamina(this.getStamina() + regeneration);
                        Main.draw("Oh no! The troll regenerated %d points of stamina!", regeneration);
                    }
                }
            };
        } else if (result < 100) {
            return new Monster("Vampire", D6.roll(2)+6, D6.roll(4), new Weapon("bite", 2, D4, 0, false) , 200){
                @Override
                public void specialDefense(Combatant hero) {
                    if (D20.roll() < 6) {
                        hero.setStrength(hero.getStrength()-1);
                        Main.draw("Don't you just hate vampires? " +
                                "This fella just sucked away one point of strength from you!");
                    }
                }
            };
        } else {
            return new Monster("Dragon", 18, 100, new Weapon("fire", 5, D8, 0, false), 1000);
        }
    }
}
