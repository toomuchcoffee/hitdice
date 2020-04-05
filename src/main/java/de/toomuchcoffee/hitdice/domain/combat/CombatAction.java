package de.toomuchcoffee.hitdice.domain.combat;

import java.util.Optional;

public interface CombatAction {
    boolean condition(Combatant attacker, Combatant defender);
    String onSuccess(Combatant attacker, Combatant defender);

    default Optional<String> execute(Combatant attacker, Combatant defender) {
        if (condition(attacker, defender)) {
            return Optional.of(onSuccess(attacker, defender));
        }
        return Optional.empty();
    }

}
