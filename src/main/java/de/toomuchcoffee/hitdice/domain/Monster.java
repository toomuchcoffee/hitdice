package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.IntStream;

@Getter
@RequiredArgsConstructor
public class Monster implements Combatant {
    private final EventFactory<Monster> factory;
    private final String name;
    private final int level;
    private final int defense;
    private final Health health;
    private final int armorClass;
    private final List<CombatAction> combatActions;

    @Override
    public int getAttack() {
        return 8 + level;
    }

    @Override
    public int getDamageBonus() {
        return 0;
    }

    public int getValue() {
        int value = IntStream.range(0, level + 1).map(l -> (l + 1) * 5).sum();
        double factor = Math.pow(1.5, combatActions.size() - 1);
        return (int) (value * factor);
    }
}
