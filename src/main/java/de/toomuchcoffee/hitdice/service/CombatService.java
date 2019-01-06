package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Combatant;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.Weapon;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.*;
import static de.toomuchcoffee.hitdice.service.DiceService.Dice.D20;

@Service
@RequiredArgsConstructor
public class CombatService {
    public static final String CAUSED_DAMAGE_MESSAGE = "%s hit %s for %d points of damage.";

    private final DiceService diceService;

    public CombatRound fight(Hero hero, Monster monster, int round) {
        List<String> events = new ArrayList<>();
        if (round > 0) {
            events.addAll(attack(hero, monster));
            events.addAll(attack(monster, hero));
        }

        CombatResult result = ONGOING;
        if (!hero.isAlive()) {
            result = DEATH;
        } else if (won(hero, monster)) {
            result = VICTORY;
        }

        return new CombatRound(round + 1, events, result);
    }

    private List<String> attack(Combatant attacker, Combatant defender) {
        List<String> events = new ArrayList<>();
        int attackScore = Math.max(1, attacker.getDexterity().getValue() - defender.getDexterity().getBonus());
        if (diceService.roll(D20) <= attackScore) {
            int protection = defender.getArmor() != null ? defender.getArmor().getProtection() : 0;
            int damage = Math.max(0, determineDamage(attacker) - protection);
            defender.decreaseCurrentStaminaBy(damage);
            events.add(String.format(CAUSED_DAMAGE_MESSAGE, attacker.getName(), defender.getName(), damage));
            attacker.specialAttack(defender).ifPresent(events::add);
            defender.specialDefense(attacker).ifPresent(events::add);
        }
        return events;
    }

    private int determineDamage(Combatant combatant) {
        Weapon weapon = combatant.getWeapon();
        return diceService.roll(weapon.getDice(), weapon.getDiceNumber()) + weapon.getBonus() + combatant.getStrength().getBonus();
    }

    private boolean won(Hero hero, Monster monster) {
        if (!monster.isAlive()) {
            hero.increaseExperience(monster.getValue());
            return true;
        }
        return false;
    }

    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class CombatRound {
        private final int round;
        private final List<String> events;
        private final CombatResult result;
    }

    public enum CombatResult {
        ONGOING, VICTORY, DEATH
    }
}
