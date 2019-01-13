package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.Combatant.AbstractCombatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction.WeaponAttack;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.Armor.NONE;
import static de.toomuchcoffee.hitdice.domain.Weapon.FISTS;

@Getter
@Setter
public class Hero extends AbstractCombatant {
    private String name;

    private final Attribute strength;
    private final Attribute dexterity;
    private final Attribute stamina;

    private final int maxHealth;
    private int health;

    private Attack weapon;
    private Armor armor;

    private List<CombatAction> combatActions = newArrayList(new WeaponAttack());

    private int experience;

    private int level = 1;

    public Hero(int strength, int dexterity, int stamina, int health) {
        this.strength = new Attribute(strength);
        this.dexterity = new Attribute(dexterity);
        this.stamina = new Attribute(stamina);
        this.maxHealth = health;
        this.health = health;
    }

    public void increaseExperience(int experience) {
        this.experience += experience;
    }

    @Override
    public Attack getWeapon() {
        return weapon == null ? FISTS : weapon;
    }

    @Override
    public Armor getArmor() {
        return armor == null ? NONE : armor;
    }

    @Override
    public boolean isAlive() {
        return getStrength().getValue() > 0 && health > 0;
    }
}
