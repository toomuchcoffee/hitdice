package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;
import static de.toomuchcoffee.hitdice.domain.Dice.D8;
import static de.toomuchcoffee.hitdice.domain.world.EventType.MONSTER;
import static java.util.Arrays.asList;

@Getter
public class Monster implements Combatant, Event {
    private final EventType eventType = MONSTER;

    private final String name;
    private final int level;
    private final int defense;
    private final Health health;
    private final Weapon weapon;
    private final int armorClass;

    private List<CombatAction> combatActions = newArrayList(new CombatAction.WeaponAttack());

    public Monster(
            String name,
            int level,
            int defense,
            Weapon weapon,
            int armorClass,
            CombatAction... combatActions) {
        this.name = name;
        this.level = level;
        this.health = new Health(level == 0 ? D4.roll() : D8.roll(level));
        this.defense = defense;
        this.weapon = weapon;
        this.armorClass = armorClass;
        this.combatActions.addAll(asList(combatActions));
    }

    @Override
    public int getAttack() {
        return 8 + level;
    }

    @Override
    public int getDamageBonus() {
        return 0;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CustomWeapon implements Weapon {
        private final String name;
        private final int diceNumber;
        private final Dice dice;
        private final int bonus;
    }

    public int getValue() {
        int value = IntStream.range(0, level + 1).map(l -> (l + 1) * 5).sum();
        double factor = Math.pow(1.5, combatActions.size() - 1);
        return (int) (value * factor);
    }
}
