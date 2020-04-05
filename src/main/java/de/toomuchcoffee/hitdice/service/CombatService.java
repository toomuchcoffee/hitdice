package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.*;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CombatService {
    public static final String CAUSED_DAMAGE_MESSAGE = "%s hit %s for %d points of damage.";

    private final HeroService heroService;

    public CombatRound fight(Hero hero, Monster monster, int round) {
        List<String> events = new ArrayList<>();
        if (round > 0) {
            events.addAll(attack(hero, monster));
            events.addAll(attack(monster, hero));
        }

        CombatResult result = ONGOING;
        if (hero.isDefeated()) {
            result = DEATH;
        } else if (monster.isDefeated()) {
            heroService.increaseExperience(hero, monster.getValue());
            result = VICTORY;
        }

        return new CombatRound(round + 1, events, result);
    }

    private List<String> attack(Combatant attacker, Combatant defender) {
        return attacker.getCombatActions().stream()
                .map(a -> a.execute(attacker, defender))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .collect(toList());
    }

    public interface CombatAction {
        boolean condition(Combatant attacker, Combatant defender);
        String onSuccess(Combatant attacker, Combatant defender);

        default Optional<String> execute(Combatant attacker, Combatant defender) {
            if (condition(attacker, defender)) {
                return Optional.of(onSuccess(attacker, defender));
            }
            return Optional.empty();
        }

        class WeaponAttack implements CombatAction {
            public boolean condition(Combatant attacker, Combatant defender) {
                int attackScore = max(1, attacker.getAttack() - defender.getDefense());
                return D20.check(attackScore);
            }

            @Override
            public String onSuccess(Combatant attacker, Combatant defender) {
                Weapon weapon = attacker.getWeapon();
                int damage = max(1, weapon.getDice().roll(weapon.getDiceNumber())
                        + weapon.getBonus()
                        + attacker.getDamageBonus()
                        - defender.getArmorClass());
                defender.reduceHealth(damage);
                return format(CAUSED_DAMAGE_MESSAGE, attacker.getName(), defender.getName(), damage);
            }
        }
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
