package de.toomuchcoffee.hitdice.domain.combat;

import java.util.Optional;

@FunctionalInterface
public interface CombatAction {
    Optional<String> execute(Combatant attacker, Combatant defender);
}
