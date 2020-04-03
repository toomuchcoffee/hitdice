package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.Combatant.AbstractCombatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.Dice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.EventType.MONSTER;
import static de.toomuchcoffee.hitdice.service.Dice.D4;
import static de.toomuchcoffee.hitdice.service.Dice.D8;
import static java.util.Arrays.asList;

@Getter
public class Monster extends AbstractCombatant implements Event {
    private String name;

    private int level;
    private int defense;

    @Setter
    private int health;
    private final int maxHealth;

    private Weapon weapon;

    private int armorClass;

    private List<CombatAction> combatActions = newArrayList(new CombatAction.WeaponAttack());

    private int value;

    public Monster(String name, int level, int defense, Weapon weapon, int armorClass, int value, CombatAction... combatActions) {
        this.name = name;
        this.level = level;
        this.health = level == 0 ? D4.roll() : D8.roll(level);
        this.maxHealth = this.health;
        this.defense = defense;
        this.weapon = weapon;
        this.armorClass = armorClass;
        this.value = value;
        this.combatActions.addAll(asList(combatActions));
    }

    @Override
    public EventType getEventType() {
        return MONSTER;
    }

    @Override
    public int getAttack() {
        return 10 + level;
    }

    @Override
    public int getDamageBonus() {
        return 0;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NaturalWeapon implements Weapon {
        private final String name;
        private final int diceNumber;
        private final Dice dice;
        private final int bonus;
    }
}
