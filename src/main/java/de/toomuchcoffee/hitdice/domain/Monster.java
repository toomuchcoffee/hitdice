package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.Combatant.AbstractCombatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.Dice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.EventType.MONSTER;
import static de.toomuchcoffee.hitdice.service.Dice.D4;
import static de.toomuchcoffee.hitdice.service.Dice.D8;
import static java.util.Arrays.asList;

@Getter
public class Monster extends AbstractCombatant implements Event {
    private final EventType eventType = MONSTER;

    private String name;

    private int level;
    private int defense;

    @Setter
    private int health;
    private final int maxHealth;

    private Weapon weapon;

    private int armorClass;

    private List<CombatAction> combatActions = newArrayList(new CombatAction.WeaponAttack());

    public Monster(String name, int level, int defense, Weapon weapon, int armorClass, CombatAction... combatActions) {
        this.name = name;
        this.level = level;
        this.health = level == 0 ? D4.roll() : D8.roll(level);
        this.maxHealth = this.health;
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
        int value = IntStream.range(0, level+1).map(l -> (l + 1) * 5).sum();
        double factor = Math.pow(1.5, (double) combatActions.size());
        return (int) (value * factor);
    }
}
