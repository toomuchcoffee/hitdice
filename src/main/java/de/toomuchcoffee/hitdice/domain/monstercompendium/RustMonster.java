package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.HandWeapon;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.service.Dice.D20;
import static de.toomuchcoffee.hitdice.service.Dice.D6;

public class RustMonster extends Monster {
    public RustMonster() {
        super("Rust monster",
                2,
                0,
                new Monster.NaturalWeapon("tail", 1, D6, 0),
                2,
                50,
                (attacker2, defender2) -> {
                    if (defender2 instanceof Hero) {
                        Hero hero1 = (Hero) defender2;
                        if (D20.check(7)) {
                            if (hero1.getWeapon() != null && hero1.getWeapon() instanceof HandWeapon && ((HandWeapon) hero1.getWeapon()).isMetallic()) {
                                hero1.setWeapon(null);
                                return Optional.of("Oh no! The $%&§ rust monster hit your weapon and it crumbles to rust.");
                            } else if (hero1.getArmor() != null && hero1.getArmor().isMetallic()) {
                                hero1.setArmor(null);
                                return Optional.of("Friggin rust monster! It hit your armor and it crumbles to rust.");
                            }
                        }
                    }
                    return Optional.empty();
                });
    }
}
