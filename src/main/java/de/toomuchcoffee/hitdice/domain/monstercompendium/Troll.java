package de.toomuchcoffee.hitdice.domain.monstercompendium;

import de.toomuchcoffee.hitdice.domain.Monster;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.service.Dice.D10;
import static de.toomuchcoffee.hitdice.service.Dice.D3;

public class Troll extends Monster {
    public Troll() {
        super("Troll",
                3,
                -1,
                new Monster.NaturalWeapon("claws", 1, D10, 0),
                3,
                100,
                (attacker1, defender1) -> {
                    if (attacker1.getHealth() > 0 && attacker1.getHealth() < attacker1.getMaxHealth()) {
                        int regeneration = D3.roll();
                        attacker1.setHealth(Math.min(attacker1.getHealth() + regeneration, attacker1.getMaxHealth()));
                        return Optional.of(String.format("Oh no! The troll regenerated %d points of stamina!", regeneration));
                    }
                    return Optional.empty();
                });
    }
}
