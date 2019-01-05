package de.toomuchcoffee.hitdice.factories;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.Weapon;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.Weapon.LONGSWORD;
import static de.toomuchcoffee.hitdice.domain.Weapon.SHORTSWORD;
import static java.lang.String.format;

public class MonsterFactory {
    public static Monster createMonster() {
        int result = D100.roll();
        if (result < 30) {
            return new Monster("Rat", 14, 2, new Weapon("teeth", 1, D3, 0, false), 5);
        } else if (result < 55) {
            return new Monster("Goblin", D6.roll(2), D6.roll(2), SHORTSWORD, 15);
        } else if (result < 75) {
            return new Monster("Orc", D6.roll(3) - 1, D6.roll(3) + 1, LONGSWORD, 25);
        } else if (result < 90) {
            return new Monster("Rust monster", D6.roll(3), D6.roll(4), new Weapon("tail", 1, D4, 0, false), 50) {
                @Override
                public Optional<String> specialAttack(Combatant hero) {
                    if (D20.roll() < 9) {
                        if (hero.getWeapon() != null && hero.getWeapon().isMetallic()) {
                            hero.setWeapon(null);
                            return Optional.of("Oh no! The $%&§ rust monster hit your weapon and it crumbles to rust.");
                        } else if (hero.getArmor() != null && hero.getArmor().isMetallic()) {
                            hero.setArmor(null);
                            return Optional.of("Friggin rust monster! It hit your armor and it crumbles to rust.");
                        }
                    }
                    return Optional.empty();
                }
            };
        } else if (result < 97) {
            return new Monster("Troll", D6.roll(2), D6.roll(4), new Weapon("claws", 2, D6, 0, false), 100) {
                @Override
                public Optional<String> specialDefense(Combatant hero) {
                    if (this.getCurrentStamina() < this.getStamina()) {
                        int regeneration = D3.roll();
                        this.setStamina(this.getStamina() + regeneration);
                        return Optional.of(format("Oh no! The troll regenerated %d points of stamina!", regeneration));
                    }
                    return Optional.empty();
                }
            };
        } else if (result < 100) {
            return new Monster("Vampire", D6.roll(2) + 6, D6.roll(4), new Weapon("bite", 2, D4, 0, false), 200) {
                @Override
                public Optional<String> specialDefense(Combatant hero) {
                    if (D20.roll() < 6) {
                        hero.setStrength(hero.getStrength() - 1);
                        return Optional.of("Don't you just hate vampires? This fella just sucked away one point of strength from you!");
                    }
                    return Optional.empty();
                }
            };
        } else {
            return new Monster("Dragon", 18, 100, new Weapon("fire", 5, D8, 0, false), 1000);
        }
    }
}
