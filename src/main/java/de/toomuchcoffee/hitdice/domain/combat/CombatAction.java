package de.toomuchcoffee.hitdice.domain.combat;

@FunctionalInterface
public interface CombatAction {
    String execute(Combatant attacker, Combatant defender);
}
