package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.Combatant.AbstractCombatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.DiceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.Armor.NONE;
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

    @Setter
    private Attack weapon;

    @Setter
    private Armor armor = NONE;

    private List<CombatAction> combatActions = newArrayList(new CombatAction.WeaponAttack());

    private int value;

    public Monster(String name, int strength, int dexterity, int stamina, Attack weapon, int value, CombatAction... additionalActions) {
        this.name = name;
        this.strength = new Attribute(strength);
        this.dexterity = new Attribute(dexterity);
        this.stamina = new Attribute(stamina);
        this.health = stamina;
        this.weapon = weapon;
        this.value = value;
        this.combatActions.addAll(asList(additionalActions));
    }

    @Override
    public EventType getEventType() {
        return MONSTER;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NaturalWeapon implements Attack {
        private final String name;
        private final int diceNumber;
        private final DiceService.Dice dice;
        private final int bonus;
    }
}
