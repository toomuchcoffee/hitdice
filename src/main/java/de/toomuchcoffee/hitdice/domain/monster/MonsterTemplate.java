package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.CustomWeapon;
import de.toomuchcoffee.hitdice.domain.combat.HandWeapon;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import lombok.Getter;

import java.util.Iterator;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.combat.HandWeapon.*;
import static de.toomuchcoffee.hitdice.domain.monster.Frequency.*;
import static java.lang.Math.max;

@Getter
public enum MonsterTemplate {
    GIANT_RAT("Giant Rat", 0, COMMON, 4, 0,
            new WeaponAttack(new CustomWeapon("teeth", D3::roll))),

    GOBLIN("Goblin", 1, COMMON, 0, 1,
            new WeaponAttack(SHORTSWORD)),

    ORC("Orc", 2, COMMON, 0, 2,
            new WeaponAttack(MACE)),

    RUST_MONSTER("Rust monster", 3, UNCOMMON, 0, 2,
            new WeaponAttack(new CustomWeapon("tail", D6::roll)),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(7)) {
                    Hero hero = (Hero) defender;
                    Iterator<Treasure> it = hero.getEquipment().iterator();
                    while (it.hasNext()) {
                        Treasure item = it.next();
                        if (item.isMetallic()) {
                            it.remove();
                            return String.format("Oh no! The rust monster hit your %s and it crumbles to rust.", item.getName());
                        }
                    }
                }
                return null;
            }),

    GHOUL("Ghoul", 2, UNCOMMON, -1, 0,
            new WeaponAttack(new CustomWeapon("claws", D4::roll)),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(5)) {
                    Hero hero = (Hero) defender;
                    hero.getDexterity().decrease(1);
                    return "The ghoul's paralyzing touch makes you lose one point of dexterity!";
                }
                return null;
            }),

    OGRE("Ogre", 3, COMMON, -1, 2,
            new WeaponAttack(new CustomWeapon("big club", () -> D6.roll(2)))),

    SKELETON("Skeleton", 1, COMMON, -2, 3,
            new WeaponAttack(LONGSWORD)),

    TROLL("Troll", 4, UNCOMMON, -1, 3,
            new WeaponAttack(new CustomWeapon("claws", D10::roll)),
            (attacker, defender) -> {
                if (attacker.getHealth().isInjured()) {
                    int regeneration = D3.roll();
                    attacker.getHealth().increase(regeneration);
                    return String.format("Oh no! The troll regenerated %d points of stamina!", regeneration);
                }
                return null;
            }),

    OOZE("Ooze", 5, RARE, -1, 1,
            new WeaponAttack(new CustomWeapon("acid", D6::roll)),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(5)) {
                    Hero hero = (Hero) defender;
                    hero.getStamina().decrease(1);
                    return "The ooze's acid harms you in such a bad way, that you lose one point of stamina!";
                }
                return null;
            }),

    VAMPIRE("Vampire", 5, RARE, 2, 0,
            new WeaponAttack(new CustomWeapon("bite", () -> D4.roll(2))),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(5)) {
                    Hero hero = (Hero) defender;
                    hero.getStrength().decrease(1);
                    return "Don't you just hate vampires? This fella just sucked away one point of strength from you!";
                }
                return null;
            }),

    ETTIN("Ettin", 4, UNCOMMON, -1, 3,
            new WeaponAttack(new CustomWeapon("big club", () -> D6.roll(2))),
            new WeaponAttack(new CustomWeapon("other big club", () -> D6.roll(2)))),

    LICH("Lich", 7, VERY_RARE, 0, 0,
            new WeaponAttack(new CustomWeapon("touch", D6::roll)),
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

    MIND_FLAYER("Mind Flayer", 6, VERY_RARE, 4, 0,
            (attacker, defender) -> {
                int attackScore = max(1, attacker.getAttack() - defender.getDefense());
                if (D20.check(attackScore)) {
                    Hero hero = (Hero) defender;
                    HandWeapon weapon = hero.getWeapon();
                    int damage = max(1, weapon.getDamage().get()
                            + defender.getDamageBonus()
                            - defender.getArmorClass());
                    defender.reduceHealth(damage);
                    return String.format("%s hit himself for %d points of damage.", defender.getName(), damage);
                }
                return null;
            }),

    BEHOLDER("Beholder", 6, VERY_RARE, 0, 0,
            new WeaponAttack(new CustomWeapon("bite", () -> D4.roll(3))),
            (attacker, defender) -> {
                Hero hero = (Hero) defender;
                int eyes = D12.roll();
                hero.reduceHealth(eyes);
                return String.format("%d of the Beholder's eyes beam right into your soul and reduce your health by %d points!", eyes, eyes);
            }),

    DEMOGORGON("Demogorgon", 7, VERY_RARE, 2, 0,
            new WeaponAttack(new CustomWeapon("bite", () -> D8.roll(2))),
            (attacker, defender) -> {
                if (defender instanceof Hero && D20.check(10)) {
                    Hero hero = (Hero) defender;
                    int damage = D12.roll();
                    hero.reduceHealth(damage);
                    return String.format("The Demogorgon's tentacles embrace you and sending out electric shocks, causing %d extra points of damage!", damage);
                }
                return null;
            }),

    DRAGON("Dragon", 8, VERY_RARE, 0, 5, new WeaponAttack(
            new CustomWeapon("claws", () -> D8.roll(2))),
            (attacker, defender) -> {
                if (D20.check(5)) {
                    int damage = D8.roll(4);
                    defender.reduceHealth(damage);
                    return String.format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage);
                }
                return null;
            });

    private final String name;
    private final int level;
    private final Frequency frequency;
    private final int defense;
    private final int armorClass;
    private final CombatAction[] combatActions;

    MonsterTemplate(String name, int level, Frequency frequency, int defense, int armorClass, CombatAction... combatAction) {
        this.name = name;
        this.level = level;
        this.frequency = frequency;
        this.defense = defense;
        this.armorClass = armorClass;
        this.combatActions = combatAction;
    }
}
