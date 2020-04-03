package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.*;
import de.toomuchcoffee.hitdice.domain.Monster.NaturalWeapon;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Armor.*;
import static de.toomuchcoffee.hitdice.domain.HandWeapon.*;
import static de.toomuchcoffee.hitdice.domain.Potion.Type.HEALING;
import static de.toomuchcoffee.hitdice.domain.Potion.Type.STRENGTH;
import static de.toomuchcoffee.hitdice.service.DiceService.Dice.*;
import static java.lang.Math.min;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DungeonService {

    private final DiceService diceService;

    private static final Random RANDOM = new Random();

    public Dungeon create(int size) {
        Dungeon dungeon = new Dungeon(size);
        initPois(dungeon);
        Position start = getAnyUnoccupiedPosition(dungeon);
        dungeon.setPosition(start);
        markAsVisited(dungeon);
        return dungeon;
    }

    public Optional<Event> explore(Direction direction, Dungeon dungeon) {
        Position position = dungeon.explore(direction);
        return Optional.ofNullable(dungeon.getEvent(position));
    }

    public Position getAnyUnoccupiedPosition(Dungeon dungeon) {
        if (dungeon.getSize() == 1) {
            return new Position(0, 0);
        }

        Position pos;
        do {
            pos = new Position(RANDOM.nextInt(dungeon.getSize()), RANDOM.nextInt(dungeon.getSize()));
        } while (dungeon.getEventMap()[pos.getX()][pos.getY()] != null);
        return pos;
    }

    public void markAsVisited(Dungeon dungeon) {
        dungeon.getVisited()[dungeon.getPosX()][dungeon.getPosY()] = true;
        dungeon.getEventMap()[dungeon.getPosX()][dungeon.getPosY()] = null;
    }

    public void collectTreasure(Hero hero, Treasure treasure) {
        if (treasure instanceof Armor) {
            hero.setArmor((Armor) treasure);
        } else if (treasure instanceof HandWeapon) {
            hero.setWeapon((HandWeapon) treasure);
        }
    }

    public void drinkPotion(Hero hero, Potion potion) {
        switch (potion.getType()) {
            case HEALING:
                hero.setHealth(min(hero.getHealth() + potion.getPower(), hero.getMaxHealth()));
                break;
            case STRENGTH:
                hero.getStrength().increase(potion.getPower());
                break;
        }
    }

    private void initPois(Dungeon dungeon) {
        for (int x = 0; x < dungeon.getSize(); x++) {
            for (int y = 0; y < dungeon.getSize(); y++) {
                dungeon.getEventMap()[x][y] = createEvent();
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getEventMap()[door.getX()][door.getY()] = Event.MAGIC_DOOR_EVENT;
    }

    private Event createEvent() {
        int d = diceService.roll(D100);
        if (d < 2) {
            return new Potion(diceService.roll(D3), STRENGTH);
        } if (d < 9) {
            return new Potion(diceService.roll(D4, 2), HEALING);
        } else if (d < 14) {
            return createTreasure();
        } else if (d < 20) {
            return createMonster();
        } else {
            return null;
        }
    }

    private Monster createMonster() {
        int result = diceService.roll(D100);
        if (result < 30) {
            return new Monster("Rat", 0, 2, 4, new NaturalWeapon("teeth", 1, D3, 0), 0, 5);
        } else if (result < 55) {
            return new Monster("Goblin", 1, diceService.roll(D6, 2), 0, SHORTSWORD, 1, 15);
        } else if (result < 75) {
            return new Monster("Orc", 2, diceService.roll(D6, 3), 0, LONGSWORD, 2, 25);
        } else if (result < 90) {
            return new Monster(
                    "Rust monster",
                    2,
                    diceService.roll(D6, 3),
                    0,
                    new NaturalWeapon("tail", 1, D4, 0),
                    2,
                    50,
                    (CombatAction) (attacker, defender, diceService) -> {
                        if (defender instanceof Hero) {
                            Hero hero = (Hero) defender;
                            if (diceService.roll(D20) < 7) {
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
        } else if (result < 97) {
            return new Monster(
                    "Troll",
                    3,
                    diceService.roll(D6, 4),
                    -1,
                    new NaturalWeapon("claws", 1, D10, 0),
                    3,
                    100,
                    (CombatAction) (attacker, defender, diceService) -> {
                        if (attacker.getHealth() > 0 && attacker.getHealth() < attacker.getMaxHealth()) {
                            int regeneration = diceService.roll(D3);
                            attacker.setHealth(min(attacker.getHealth() + regeneration, attacker.getMaxHealth()));
                            return Optional.of(format("Oh no! The troll regenerated %d points of stamina!", regeneration));
                        }
                        return Optional.empty();
                    });
        } else if (result < 100) {
            return new Monster(
                    "Vampire",
                    5,
                    diceService.roll(D6, 6),
                    2,
                    new NaturalWeapon("bite", 2, D4, 0),
                    0,
                    200,
                    (CombatAction) (attacker, defender, diceService) -> {
                        if (diceService.roll(D20) < 5) {
                            if (defender instanceof Hero) {
                                Hero hero = (Hero) defender;
                                hero.getStrength().decrease();
                                return Optional.of("Don't you just hate vampires? This fella just sucked away one point of strength from you!");
                            }
                        }
                        return Optional.empty();
                    });
        } else {
            return new Monster(
                    "Dragon",
                    20,
                    100,
                    0,
                    new NaturalWeapon("claws", 2, D8, 0),
                    5,
                    1000,
                    (CombatAction) (attacker, defender, diceService) -> {
                        if (diceService.roll(D20) < 7) {
                            int damage = diceService.roll(D8, 5);
                            defender.reduceHealth(damage);
                            return Optional.of(format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage));
                        }
                        return Optional.empty();
                    });
        }
    }

    private Treasure createTreasure() {
        int result = diceService.roll(D100);
        if (result < 12) {
            return DAGGER;
        } else if (result < 24) {
            return CLUB;
        } else if (result < 36) {
            return STAFF;
        } else if (result < 48) {
            return LEATHER;
        } else if (result < 60) {
            return SHORTSWORD;
        } else if (result < 70) {
            return MACE;
        } else if (result < 80) {
            return CHAIN;
        } else if (result < 88) {
            return LONGSWORD;
        } else if (result < 96) {
            return PLATE;
        } else {
            return CLAYMORE;
        }
    }
}
