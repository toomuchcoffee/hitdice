package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.attribute.AbstractAttribute;
import de.toomuchcoffee.hitdice.domain.attribute.Attribute;
import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.Attack;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.equipment.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static de.toomuchcoffee.hitdice.domain.Dice.D8;
import static de.toomuchcoffee.hitdice.domain.Dice._3D6;
import static de.toomuchcoffee.hitdice.domain.attribute.AttributeType.*;
import static de.toomuchcoffee.hitdice.domain.equipment.Weapon.FISTS;
import static java.lang.Math.max;
import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class Hero implements Combatant {
    private String name;

    private SortedMap<AttributeType, AbstractAttribute> attributes = new TreeMap<>();

    private List<Item> equipment = new ArrayList<>();

    private int experience;

    private int level;

    private boolean currentDungeonExplored;

    public void initialize() {
        attributes = new TreeMap<>();
        attributes.put(STRENGTH, new Attribute(_3D6.roll()));
        attributes.put(DEXTERITY, new Attribute(_3D6.roll()));
        attributes.put(STAMINA, new Attribute(_3D6.roll()));
        attributes.put(HEALTH, new Health(0));
        this.levelUp();
    }

    public void initialize(int strength, int dexterity, int stamina, int health, int maxHealth) {
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
        return getDexterity().getBonus() + Optional.ofNullable(getShield())
                .map(Shield::getDefense)
                .orElse(0);
    }

    public List<CombatAction> getCombatActions() {
        return List.of(Attack.with(getWeapon()));
    }


    @Override
    public int getDamageBonus() {
        return getStrength().getBonus();
    }

    public Weapon getWeapon() {
        return getEquipment(Weapon.class, FISTS);
    }

    public Armor getArmor() {
        return getEquipment(Armor.class, null);
    }

    public Shield getShield() {
        return getEquipment(Shield.class, null);
    }

    private <T extends Item> T getEquipment(Class<T> clazz, T defaultValue) {
        return equipment.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .max(Comparator.comparingInt(Item::getOrdinal))
                .orElse(defaultValue);
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
                .filter(e -> !e.equals(getArmor()) && !e.equals(getWeapon()) && !e.equals(getShield()) && !(e instanceof Potion))
                .collect(toList());
    }

    @Override
    public boolean isDefeated() {
        return attributes.values().stream().anyMatch(v -> v.getValue() <= 0);
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
