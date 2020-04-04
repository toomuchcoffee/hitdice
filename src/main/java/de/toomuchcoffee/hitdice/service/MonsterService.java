package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.HandWeapon;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.HandWeapon.MACE;
import static de.toomuchcoffee.hitdice.domain.HandWeapon.SHORTSWORD;
import static de.toomuchcoffee.hitdice.service.Dice.*;

@Service
public class MonsterService {

    public static final Monster VAMPIRE = new Monster(
            "Vampire",
            5,
            2,
            new Monster.NaturalWeapon("bite", 2, D4, 0),
            0,
            200,
            (attacker, defender) -> {
                if (D20.check(5)) {
                    if (defender instanceof Hero) {
                        Hero hero = (Hero) defender;
                        hero.getStrength().decrease();
                        return Optional.of("Don't you just hate vampires? This fella just sucked away one point of strength from you!");
                    }
                }
                return Optional.empty();
            });
    public static final Monster DRAGON = new Monster(
            "Dragon",
            8,
            0,
            new Monster.NaturalWeapon("claws", 1, D8, 0),
            5,
            400,
            (attacker, defender) -> {
                if (D20.check(5)) {
                    int damage = D8.roll(2);
                    defender.reduceHealth(damage);
                    return Optional.of(String.format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage));
                }
                return Optional.empty();
            });
    public static final Monster TROLL = new Monster(
            "Troll",
            3,
            -1,
            new Monster.NaturalWeapon("claws", 1, D10, 0),
            3,
            100,
            (attacker, defender) -> {
                if (attacker.getHealth() > 0 && attacker.getHealth() < attacker.getMaxHealth()) {
                    int regeneration = D3.roll();
                    attacker.setHealth(Math.min(attacker.getHealth() + regeneration, attacker.getMaxHealth()));
                    return Optional.of(String.format("Oh no! The troll regenerated %d points of stamina!", regeneration));
                }
                return Optional.empty();
            });
    public static final Monster RUST_MONSTER = new Monster(
            "Rust monster",
            2,
            0,
            new Monster.NaturalWeapon("tail", 1, D6, 0),
            2,
            50,
            (attacker, defender) -> {
                if (defender instanceof Hero) {
                    Hero hero = (Hero) defender;
                    if (D20.check(7)) {
                        if (hero.getWeapon() != null && hero.getWeapon() instanceof HandWeapon && ((HandWeapon) hero.getWeapon()).isMetallic()) {
                            hero.setWeapon(null);
                            return Optional.of("Oh no! The $%&§ rust monster hit your weapon and it crumbles to rust.");
                        } else if (hero.getArmor() != null && hero.getArmor().isMetallic()) {
                            hero.setArmor(null);
                            return Optional.of("Friggin rust monster! It hit your armor and it crumbles to rust.");
                        }
                    }
                }
                return Optional.empty();
            });
    public static final Monster GHOUL = new Monster(
            "Ghoul",
            2,
            -1,
            new Monster.NaturalWeapon("claws", 1, D4, 0),
            0,
            40,
            (attacker, defender) -> {
                if (D20.check(5)) {
                    if (defender instanceof Hero) {
                        Hero hero = (Hero) defender;
                        hero.getStamina().decrease();
                        return Optional.of("Oh my, the foulness of the Ghoul has drained your stamina by one point!");
                    }
                }
                return Optional.empty();
            });
    public static final Monster ORC = new Monster("Orc", 2, 0, MACE, 2, 25);
    public static final Monster GOBLIN = new Monster("Goblin", 1, 0, SHORTSWORD, 1, 15);
    public static final Monster GIANT_RAT = new Monster("Giant Rat", 0, 4, new Monster.NaturalWeapon("teeth", 1, D3, 0), 0, 5);

    public Monster createMonster() {
        switch (D100.roll()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                return GIANT_RAT;
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                return GOBLIN;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
                return ORC;
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
                return GHOUL;
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
                return RUST_MONSTER;
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
                return TROLL;
            case 95:
            case 96:
            case 97:
            case 98:
                return VAMPIRE;
            case 99:
            case 100:
                return DRAGON;
            default:
                return null;
        }
    }
}