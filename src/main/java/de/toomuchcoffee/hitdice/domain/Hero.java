package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.attribute.AbstractAttribute;
import de.toomuchcoffee.hitdice.domain.attribute.Attribute;
import de.toomuchcoffee.hitdice.domain.attribute.AttributeName;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;
import de.toomuchcoffee.hitdice.domain.item.Armor;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;
import static de.toomuchcoffee.hitdice.domain.Dice.D8;
import static de.toomuchcoffee.hitdice.domain.attribute.AttributeName.*;
import static de.toomuchcoffee.hitdice.domain.combat.Weapon.FISTS;
import static java.lang.Math.max;

@Getter
@Setter
public class Hero implements Combatant {
    private String name;

    private SortedMap<AttributeName, AbstractAttribute> attributes = new TreeMap<>();

    private List<Treasure> equipment = new ArrayList<>();

    private int experience;

    private int level;

    public void initialize() {
        attributes = new TreeMap<>();
        attributes.put(STRENGTH, new Attribute(D6.roll(3)));
        attributes.put(DEXTERITY, new Attribute(D6.roll(3)));
        attributes.put(STAMINA, new Attribute(D6.roll(3)));
        attributes.put(HEALTH, new Health(0));
        this.levelUp();
    }

    public void initializeWithPresets(int strength, int dexterity, int stamina, int health, int maxHealth) {
        attributes.put(STRENGTH, new Attribute(strength));
        attributes.put(DEXTERITY, new Attribute(dexterity));
        attributes.put(STAMINA, new Attribute(stamina));
        attributes.put(HEALTH, new Health(health, maxHealth));
    }

    public void levelUp() {
        level++;
        int healthIncrease = max(1, D8.roll() + getStamina().getBonus());
        getHealth().raiseMaxValue(healthIncrease);
    }

    @Override
    public int getAttack() {
        return 8 + level + getDexterity().getBonus();
    }

    @Override
    public int getDefense() {
        return getDexterity().getBonus();
    }

    public List<CombatAction> getCombatActions() {
        return newArrayList(new WeaponAttack(getWeapon()));
    }


    @Override
    public int getDamageBonus() {
        return getStrength().getBonus();
    }

    public Weapon getWeapon() {
        return equipment.stream()
                .filter(i -> i instanceof Weapon)
                .map(i -> (Weapon) i)
                .findFirst()
                .orElse(FISTS);
    }

    public Armor getArmor() {
        return equipment.stream()
                .filter(i -> i instanceof Armor)
                .map(i -> (Armor) i)
                .findFirst()
                .orElse(null);
    }

    @Override
    public int getArmorClass() {
        return Optional.ofNullable(getArmor())
                .map(Armor::getProtection)
                .orElse(0);
    }

    public void addEquipment(Treasure item) {
        equipment.add(0, item);
    }

    @Override
    public boolean isDefeated() {
        return attributes.values().stream().anyMatch(v -> v.getValue() <= 0);
    }

    public void drink(Potion potion) {
        attributes.get(potion.getType()).increase(potion.getPower());
    }

    public Attribute getStrength() {
        return (Attribute) attributes.get(STRENGTH);
    }

    public Attribute getDexterity() {
        return (Attribute) attributes.get(DEXTERITY);
    }

    public Attribute getStamina() {
        return (Attribute) attributes.get(STAMINA);
    }

    public Health getHealth() {
        return (Health) attributes.get(HEALTH);
    }

}
