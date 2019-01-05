package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.*;

@Service
public class CombatService {
    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class CombatRound {
        private final int round;
        private final Integer damageCaused;
        private final Integer damageReceived;
        private final CombatResult result;
    }

    public enum CombatResult {
        ONGOING, VICTORY, DEATH
    }

    public CombatRound fight(Hero hero, Monster monster, int round) {
        Integer damageCaused = null;
        Integer damageReceived = null;

        if (round > 0) {
            damageCaused = attack(hero, monster);
            damageReceived = attack(monster, hero);
        }

        CombatResult result = ONGOING;
        if (!hero.isAlive()) {
            result = DEATH;
        } else if (won(hero, monster)) {
            result = VICTORY;
        }

        return new CombatRound(round + 1, damageCaused, damageReceived, result);
    }

    private Integer attack(Combatant attacker, Combatant defender) {
        int attackScore = Math.max(1, attacker.getDexterity() - defender.getAttributeBonus(defender.getDexterity()));
        if (D20.roll() <= attackScore) {
            int protection = defender.getArmor() != null ? defender.getArmor().getProtection() : 0;
            int damage = Math.max(0, attacker.damage() - protection);
            defender.decreaseCurrentStaminaBy(damage);
            //attacker.specialAttack(defender);
            //defender.specialDefense(attacker);
            return damage;
        }
        return null;
    }

    private boolean won(Hero hero, Monster monster) {
        if (!monster.isAlive()) {
            hero.increaseExperience(monster.getValue());
            return true;
        }
        return false;
    }
}
