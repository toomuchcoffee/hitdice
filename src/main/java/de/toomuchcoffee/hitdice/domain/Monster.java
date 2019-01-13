package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.Combatant.AbstractCombatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.DiceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.EventType.MONSTER;
import static java.util.Arrays.asList;

@Getter
public class Monster extends AbstractCombatant implements Event {
    private String name;

    private Attribute strength;
    private Attribute dexterity;
    private Attribute stamina;

    @Setter
    private int health;

    private Weapon weapon;

    private int armorClass;

    private List<CombatAction> combatActions = newArrayList(new CombatAction.WeaponAttack());

    private int value;

    public Monster(String name, int strength, int dexterity, int stamina, Weapon weapon, int armorClass, int value, CombatAction... additionalActions) {
        this.name = name;
        this.strength = new Attribute(strength);
        this.dexterity = new Attribute(dexterity);
        this.stamina = new Attribute(stamina);
        this.health = stamina;
        this.weapon = weapon;
        this.armorClass = armorClass;
        this.value = value;
        this.combatActions.addAll(asList(additionalActions));
    }

    @Override
    public EventType getEventType() {
        return MONSTER;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NaturalWeapon implements Weapon {
        private final String name;
        private final int diceNumber;
        private final DiceService.Dice dice;
        private final int bonus;
    }
}
