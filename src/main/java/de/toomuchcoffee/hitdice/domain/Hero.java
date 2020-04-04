package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.Combatant.AbstractCombatant;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction.WeaponAttack;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.HandWeapon.FISTS;
import static de.toomuchcoffee.hitdice.service.Dice.D6;
import static de.toomuchcoffee.hitdice.service.Dice.D8;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
public class Hero extends AbstractCombatant {
    private String name;

    private Attribute strength;
    private Attribute dexterity;
    private Attribute stamina;

    private int maxHealth;
    private int health;

    private Weapon weapon;
    private Armor armor;

    private List<CombatAction> combatActions = newArrayList(new WeaponAttack());

    private int experience;

    private int level;

    public Hero() {
    }

    public void initialize() {
        this.strength = new Attribute(D6.roll(3));
        this.dexterity = new Attribute(D6.roll(3));
        this.stamina = new Attribute(D6.roll(3));
        this.levelUp();
    }

    public void initializeWithPresets(int strength, int dexterity, int stamina) {
        this.strength = new Attribute(strength);
        this.dexterity = new Attribute(dexterity);
        this.stamina = new Attribute(stamina);
    }

    public void levelUp() {
        level++;
        int healthIncrease = max(1, D8.roll() + stamina.getBonus());
        maxHealth += healthIncrease;
        health += healthIncrease;
    }

    @Override
    public int getAttack() {
        return 8 + level + dexterity.getBonus();
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
        return getStrength().getValue() > 0 && getStamina().getValue() > 0 && health > 0;
    }

    public void drink(Potion potion) {
        switch (potion.getType()) {
            case HEALING:
                setHealth(min(getHealth() + potion.getPower(), getMaxHealth()));
                break;
            case STRENGTH:
                getStrength().increase(potion.getPower());
            case DEXTERITY:
                getDexterity().increase(potion.getPower());
                break;
            case DEXTERITY:
                getDexterity().increase(potion.getPower());
                break;
            case STAMINA:
                getStamina().increase(potion.getPower());
                break;
        }
    }
}
