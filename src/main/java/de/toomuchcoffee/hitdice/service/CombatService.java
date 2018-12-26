package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import org.springframework.stereotype.Service;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;

@Service
public class CombatService {

    public Integer attack(Combatant attacker, Combatant defender) {
        int attackScore = Math.max(1, attacker.getDexterity() - defender.getAttributeBonus(defender.getDexterity()));
        if (D20.roll() <= attackScore) {
            int protection = defender.getArmor() != null ? defender.getArmor().getProtection() : 0;
            int damage = Math.max(0, attacker.damage() - protection);
            defender.decreaseCurrentStaminaBy(damage);
            return damage;
            // TODO
            //attacker.specialAttack(defender);
            //defender.specialDefense(attacker);
        }
        return null;
    }

    public void won(Hero hero, Monster monster) {
        if (!monster.isAlive()) {
            hero.increaseExperience(monster.getValue());
        }
    }
}
