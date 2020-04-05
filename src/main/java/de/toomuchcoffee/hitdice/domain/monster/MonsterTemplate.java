package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.*;
import lombok.Getter;

import static de.toomuchcoffee.hitdice.domain.Dice.*;
import static de.toomuchcoffee.hitdice.domain.combat.Weapon.*;
import static de.toomuchcoffee.hitdice.domain.monster.Frequency.*;
import static java.lang.Math.max;

@Getter
public enum MonsterTemplate {
    GIANT_RAT("Giant Rat", 0, COMMON, 4, 0, new WeaponAttack(new CustomWeapon("teeth", D3::roll))),
    GOBLIN("Goblin", 1, COMMON, 0, 1, new WeaponAttack(SHORTSWORD)),
    ORC("Orc", 2, COMMON, 0, 2, new WeaponAttack(MACE)),
    RUST_MONSTER("Rust monster", 3, UNCOMMON, 0, 2, new WeaponAttack(new CustomWeapon("tail", D6::roll)), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return defender instanceof Hero && D20.check(7);
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            if (hero.getWeapon() != null && hero.getWeapon().isMetallic()) {
                hero.setWeapon(null);
                return "Oh no! The $%&§ rust monster hit your weapon and it crumbles to rust.";
            } else if (hero.getArmor() != null && hero.getArmor().isMetallic()) {
                hero.setArmor(null);
                return "Friggin rust monster! It hit your armor and it crumbles to rust.";
            } else {
                return "The rust monster has destroyed all your metal, so no harm done this time!";
            }
        }
    }),
    GHOUL("Ghoul", 2, UNCOMMON, -1, 0, new WeaponAttack(new CustomWeapon("claws", D4::roll)), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return defender instanceof Hero && D20.check(5);
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            hero.getDexterity().decrease(1);
            return "The ghoul's paralyzing touch makes you lose one point of dexterity!";
        }
    }),
    OGRE("Ogre", 3, COMMON, -1, 2, new WeaponAttack(new CustomWeapon("big club", () -> D6.roll(2)))),
    SKELETON("Skeleton", 1, COMMON, -2, 3, new WeaponAttack(LONGSWORD)),
    TROLL("Troll", 4, UNCOMMON, -1, 3, new WeaponAttack(new CustomWeapon("claws", D10::roll)), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return attacker.getHealth().isInjured();
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            int regeneration = D3.roll();
            attacker.getHealth().increase(regeneration);
            return String.format("Oh no! The troll regenerated %d points of stamina!", regeneration);
        }
    }),
    OOZE("Ooze", 5, RARE, -1, 1, new WeaponAttack(new CustomWeapon("acid", D6::roll)), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return defender instanceof Hero && D20.check(5);
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            hero.getStamina().decrease(1);
            return "The ooze's acid harms you in such a bad way, that you lose one point of stamina!";
        }
    }),
    VAMPIRE("Vampire", 5, RARE, 2, 0, new WeaponAttack(new CustomWeapon("bite", () -> D4.roll(2))), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return defender instanceof Hero && D20.check(5);
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            hero.getStrength().decrease(1);
            return "Don't you just hate vampires? This fella just sucked away one point of strength from you!";
        }
    }),
    ETTIN("Ettin", 4, UNCOMMON, -1, 3, new WeaponAttack(new CustomWeapon("big club", () -> D6.roll(2))), new WeaponAttack(new CustomWeapon("other big club", () -> D6.roll(2)))),
    LICH("Lich", 7, VERY_RARE, 0, 0, new WeaponAttack(new CustomWeapon("touch", D6::roll)), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return defender instanceof Hero && D20.check(4);
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            hero.getStrength().decrease(1);
            hero.getDexterity().decrease(1);
            hero.getStamina().decrease(1);
            return "The eternal coldness of the Lich's touch weakens you. You lose 1 point on each attribute score!";
        }
    }),
    MIND_FLAYER("Mind Flayer", 6, VERY_RARE, 4, 0, new CombatAction() {
        public boolean condition(Combatant attacker, Combatant defender) {
            int attackScore = max(1, attacker.getAttack() - defender.getDefense());
            return D20.check(attackScore);
        }

        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            Weapon weapon = hero.getWeapon();
            int damage = max(1, weapon.getDamage().get()
                    + defender.getDamageBonus()
                    - defender.getArmorClass());
            defender.reduceHealth(damage);
            return String.format("%s hit himself for %d points of damage.", defender.getName(), damage);
        }
    }),
    BEHOLDER("Beholder", 6, VERY_RARE, 0, 0, new WeaponAttack(new CustomWeapon("bite", () -> D4.roll(3))), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return true;
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            int eyes = D12.roll();
            hero.reduceHealth(eyes);
            return String.format("%d of the Beholder's eyes beam right into your soul and reduce your health by %d points!", eyes, eyes);
        }
    }),
    DEMOGORGON("Demogorgon", 7, VERY_RARE, 2, 0, new WeaponAttack(new CustomWeapon("bite", () -> D8.roll(2))), new CombatAction() {
        @Override
        public boolean condition(Combatant attacker, Combatant defender) {
            return defender instanceof Hero && D20.check(10);
        }

        @Override
        public String onSuccess(Combatant attacker, Combatant defender) {
            Hero hero = (Hero) defender;
            int damage = D12.roll();
            hero.reduceHealth(damage);
            return String.format("The Demogorgon's tentacles embrace you and sending out electric shocks, causing %d extra points of damage!", damage);
        }
    }),
    DRAGON("Dragon", 8, VERY_RARE, 0, 5, new WeaponAttack(new CustomWeapon("claws", () -> D8.roll(2))), new

            CombatAction() {
                @Override
                public boolean condition(Combatant attacker, Combatant defender) {
                    return D20.check(5);
                }

                @Override
                public String onSuccess(Combatant attacker, Combatant defender) {
                    int damage = D8.roll(4);
                    defender.reduceHealth(damage);
                    return String.format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage);
                }
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
