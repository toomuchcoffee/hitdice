package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.IntStream;

import static de.toomuchcoffee.hitdice.domain.world.EventType.MONSTER;

@Getter
@Builder
public class Monster implements Combatant {
    private final EventType eventType = MONSTER;

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
