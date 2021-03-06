package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import de.toomuchcoffee.hitdice.domain.combat.Attack;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.equipment.Weapon;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;

import java.util.Iterator;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.*;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.Arrays.asList;

@Getter
public enum MonsterFactory implements EventFactory<Monster> {
    GIANT_RAT("Giant Rat", 0, COMMON, 4, 0,
            Attack.with(Weapon.simple("teeth", D3))),

    GOBLIN("Goblin", 1, COMMON, 0, 1,
            Attack.with(Weapon.simple("scimitar", D6))),

    ORC("Orc", 2, COMMON, 0, 2,
            Attack.with(Weapon.simple("great scimitar", D8))),

    RUST_MONSTER("Rust monster", 3, UNCOMMON, 0, 2,
            Attack.with(Weapon.simple("tail", D6)),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(7)) {
                    Hero hero = (Hero) defender;
                    Iterator<Item> it = hero.getEquipment().iterator();
                    while (it.hasNext()) {
                        Item item = it.next();
                        if (item.isMetallic()) {
                            it.remove();
                            return format("Oh no! The Rust Monster hit your %s and it crumbles to rust.", item.getDisplayName());
                        }
                    }
                }
                return null;
            }),

    ZOMBIE("Zombie", 2, COMMON, -1, 0,
            Attack.with(Weapon.simple("claws", D4)),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(5)) {
                    Hero hero = (Hero) defender;
                    hero.getStamina().decrease(1);
                    return "The Zombie is so disgusting that it makes you lose one point of stamina!";
                }
                return null;
            }),

    GHOUL("Ghoul", 2, UNCOMMON, -1, 0,
            Attack.with(Weapon.simple("claws", D4)),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(5)) {
                    Hero hero = (Hero) defender;
                    hero.getDexterity().decrease(1);
                    return "The Ghoul's paralyzing touch makes you lose one point of dexterity!";
                }
                return null;
            }),

    OGRE("Ogre", 3, COMMON, -1, 2,
            Attack.with(Weapon.simple("big club", n(2).D(6)))),

    SKELETON("Skeleton", 1, COMMON, 0, 2,
            Attack.with(Weapon.simple("ancient sword", D8))),

    TROLL("Troll", 4, UNCOMMON, -1, 3,
            Attack.with(Weapon.simple("claws", D10)),
            (attacker, defender) -> {
                if (attacker.getHealth().isInjured()) {
                    int regeneration = D3.roll();
                    attacker.getHealth().increase(regeneration);
                    return format("Oh no! The Troll regenerated %d points of stamina!", regeneration);
                }
                return null;
            }),

    OOZE("Ooze", 5, RARE, -1, 1,
            Attack.with(Weapon.simple("acid", n(3).D(4))),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(7)) {
                    Hero hero = (Hero) defender;
                    Iterator<Item> it = hero.getEquipment().iterator();
                    while (it.hasNext()) {
                        Item item = it.next();
                        if (!item.isMetallic()) {
                            it.remove();
                            return format("The Ooze swallows your %s and destroys it with its acid", item.getDisplayName());
                        }
                    }
                }
                return null;
            }),

    VAMPIRE("Vampire", 5, RARE, 2, 0,
            Attack.with(Weapon.simple("bite", n(2).D(4))),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(5)) {
                    Hero hero = (Hero) defender;
                    hero.getStrength().decrease(1);
                    return "The Vampire sucked one point of strength from you!";
                }
                return null;
            }),

    ETTIN("Ettin", 4, UNCOMMON, -1, 3,
            Attack.with(Weapon.simple("big club", n(2).D(6))),
            Attack.with(Weapon.simple("other big club", n(2).D(6)))),


    MIND_FLAYER("Mind Flayer", 6, VERY_RARE, 4, 0,
            (attacker, defender) -> {
                int attackScore = max(1, attacker.getAttack() - defender.getDefense());
                if (D20.check(attackScore)) {
                    Hero hero = (Hero) defender;
                    Weapon weapon = hero.getWeapon();
                    int damage = max(1, weapon.getDamage().roll()
                            + defender.getDamageBonus()
                            - defender.getArmorClass());
                    defender.reduceHealth(damage);
                    return format("%s hit himself for %d points of damage.", defender.getName(), damage);
                }
                return null;
            }),

    BEHOLDER("Beholder", 6, VERY_RARE, 0, 0,
            Attack.with(Weapon.simple("bite", n(3).D(4))),
            (attacker, defender) -> {
                Hero hero = (Hero) defender;
                int eyes = D20.roll() - 9;
                if (eyes <= 0) {
                    return null;
                }
                int damage = n(eyes).D(4).roll();
                hero.reduceHealth(eyes);
                return format("%d of the Beholder's eyes beam right into your soul and reduce your health by %d points!", eyes, damage);
            }),

    LICH("Lich", 7, VERY_RARE, 0, 3,
            Attack.with(Weapon.simple("two-handed sword", D12)),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(4)) {
                    Hero hero = (Hero) defender;
                    hero.getStrength().decrease(1);
                    hero.getDexterity().decrease(1);
                    hero.getStamina().decrease(1);
                    return "The eternal coldness of the Lich's touch weakens you. You lose 1 point on each attribute score!";
                }
                return null;
            }),

    DEMOGORGON("Demogorgon", 7, VERY_RARE, 2, 0,
            Attack.with(Weapon.simple("bite", n(2).D(8))),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(10)) {
                    Hero hero = (Hero) defender;
                    int nrOfTentacles = D3.roll();
                    int damage = n(nrOfTentacles).D(12).roll();
                    hero.reduceHealth(damage);
                    return format("%d of the Demogorgon's tentacles embrace you and send out electric shocks, causing %d extra points of damage!", nrOfTentacles, damage);
                }
                return null;
            }),

    DRAGON("Dragon", 8, VERY_RARE, 0, 5, Attack.with(
            Weapon.simple("claws", n(2).D(8))),
            (attacker, defender) -> {
                if (D20.check(10)) {
                    int damage = n(4).D(8).roll();
                    defender.reduceHealth(damage);
                    return format("The Dragon's breath causes %d points of damage", damage);
                }
                return null;
            });

    private final String name;
    private final int level;
    private final Frequency frequency;
    private final int defense;
    private final int armorClass;
    private final CombatAction[] combatActions;

    MonsterFactory(String name, int level, Frequency frequency, int defense, int armorClass, CombatAction... combatAction) {
        this.name = name;
        this.level = level;
        this.frequency = frequency;
        this.defense = defense;
        this.armorClass = armorClass;
        this.combatActions = combatAction;
    }

    @Override
    public Monster create() {
        return new Monster(
                this,
                name,
                level,
                defense,
                new Health(level == 0 ? D4.roll() : n(level).D(8).roll()),
                armorClass,
                asList(combatActions)
        );
    }
}
