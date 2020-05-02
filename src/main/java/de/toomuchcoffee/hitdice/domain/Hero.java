package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.attribute.AbstractAttribute;
import de.toomuchcoffee.hitdice.domain.attribute.Attribute;
import de.toomuchcoffee.hitdice.domain.attribute.AttributeName;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.HandWeapon;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;
import de.toomuchcoffee.hitdice.domain.equipment.Armor;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;
import static de.toomuchcoffee.hitdice.domain.Dice.D8;
import static de.toomuchcoffee.hitdice.domain.attribute.AttributeName.*;
import static de.toomuchcoffee.hitdice.domain.combat.HandWeapon.FISTS;
import static java.lang.Math.max;
import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class Hero implements Combatant {
    private String name;

    private SortedMap<AttributeName, AbstractAttribute> attributes = new TreeMap<>();

    private List<Item> equipment = new ArrayList<>();

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

    public HandWeapon getWeapon() {
        return equipment.stream()
                .filter(i -> i instanceof HandWeapon)
                .map(i -> (HandWeapon) i)
                .max(Comparator.naturalOrder())
                .orElse(FISTS);
    }

    public Armor getArmor() {
        return equipment.stream()
                .filter(i -> i instanceof Armor)
                .map(i -> (Armor) i)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    public List<Potion> getPotions() {
        return equipment.stream()
                .filter(i -> i instanceof Potion)
                .map(i -> (Potion) i)
                .collect(toList());
    }

    @Override
    public int getArmorClass() {
        return Optional.ofNullable(getArmor())
                .map(Armor::getProtection)
                .orElse(0);
    }

    public void addEquipment(Item item) {
        equipment.add(0, item);
    }

    public List<Item> getMiscellaneous() {
        return equipment.stream()
                .filter(e -> !getArmor().equals(e) && !getWeapon().equals(e) && !(e instanceof Potion))
                .collect(toList());
    }

    @Override
    public boolean isDefeated() {
        return attributes.values().stream().anyMatch(v -> v.getValue() <= 0);
    }

    public void drink(Potion potion) {
        attributes.get(potion.getType()).increase(potion.getPotency().get());
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
