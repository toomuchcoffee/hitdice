package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.Combatant.AbstractCombatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction.WeaponAttack;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.HandWeapon.FISTS;

@Getter
@Setter
public class Hero extends AbstractCombatant {
    private String name;

    private final Attribute strength;
    private final Attribute dexterity;
    private final Attribute stamina;

    private int maxHealth;
    private int health;

    private Weapon weapon;
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

    @Override
    public int getAttack() {
        return 10 + level + dexterity.getBonus();
    }

    @Override
    public int getDefense() {
        return dexterity.getBonus();
    }

    @Override
    public int getDamageBonus() {
        return strength.getBonus();
    }

    @Override
    public Weapon getWeapon() {
        return weapon == null ? FISTS : weapon;
    }

    @Override
    public int getArmorClass() {
        return armor == null ? 0 : armor.getProtection();
    }

    @Override
    public boolean isAlive() {
        return getStrength().getValue() > 0 && health > 0;
    }
}
