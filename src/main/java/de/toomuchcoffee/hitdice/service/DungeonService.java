package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.*;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.Armor.*;
import static de.toomuchcoffee.hitdice.domain.Event.*;
import static de.toomuchcoffee.hitdice.domain.EventType.*;
import static de.toomuchcoffee.hitdice.domain.Weapon.*;
import static de.toomuchcoffee.hitdice.service.DiceService.Dice.*;
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

    public Event explore(Direction direction, Dungeon dungeon, Hero hero) {
        Position position = dungeon.explore(direction);
        return dungeon.getPoi(position);
    }

    public Position getAnyUnoccupiedPosition(Dungeon dungeon) {
        if (dungeon.getSize() == 1) {
            return new Position(0, 0);
        }

        Position pos;
        do {
            pos = new Position(RANDOM.nextInt(dungeon.getSize()), RANDOM.nextInt(dungeon.getSize()));
        } while (dungeon.getEventMap()[pos.getX()][pos.getY()].getType().isOccupied());
        return pos;
    }

    public void markAsVisited(Dungeon dungeon) {
        dungeon.getEventMap()[dungeon.getPosX()][dungeon.getPosY()] = EXPLORED_EVENT;
    }

    public void collectTreasure(Hero hero, Treasure treasure) {
        if (treasure instanceof Armor) {
            hero.setArmor((Armor) treasure);
        } else if (treasure instanceof Weapon) {
            hero.setWeapon((Weapon) treasure);
        }
    }

    public void drinkPotion(Hero hero, Potion potion) {
        hero.setHealth(Math.min(hero.getHealth() + potion.getPower(), hero.getStamina().getValue()));
    }

    private void initPois(Dungeon dungeon) {
        for (int x = 0; x < dungeon.getSize(); x++) {
            for (int y = 0; y < dungeon.getSize(); y++) {
                dungeon.getEventMap()[x][y] = createEvent();
            }
        }
        Position door = getAnyUnoccupiedPosition(dungeon);
        dungeon.getEventMap()[door.getX()][door.getY()] = MAGIC_DOOR_EVENT;
    }

    private Event createEvent() {
        int d = diceService.roll(D20);
        if (d > 17) {
            return new Event(POTION, new Potion(diceService.roll(D6, 2)));
        } else if (d > 14) {
            return new Event(TREASURE, createTreasure());
        } else if (d > 11) {
            return new Event(MONSTER, createMonster());
        } else {
            return EMPTY_EVENT;
        }
    }

    private Monster createMonster() {
        int result = diceService.roll(D100);
        if (result < 30) {
            return new Monster("Rat", 2, 14, 2, new Weapon("teeth", 1, D4, 0, false), 5);
        } else if (result < 55) {
            return new Monster("Goblin", diceService.roll(D6, 2), diceService.roll(D6, 2), diceService.roll(D6, 2), SHORTSWORD, 15);
        } else if (result < 75) {
            return new Monster("Orc", diceService.roll(D6, 3) + 1, diceService.roll(D6, 3) - 1, diceService.roll(D6, 3) + 1, LONGSWORD, 25);
        } else if (result < 90) {
            return new Monster(
                    "Rust monster",
                    diceService.roll(D6, 3),
                    diceService.roll(D6, 3),
                    diceService.roll(D6, 4),
                    new Weapon("tail", 1, D4, 0, false),
                    50,
                    new CombatAction() {
                        @Override
                        public Optional<String> execute(Combatant attacker, Combatant defender, DiceService diceService) {
                            if (diceService.roll(D20) < 9) {
                                if (defender.getWeapon() != null && defender.getWeapon().isMetallic()) {
                                    defender.setWeapon(null);
                                    return Optional.of("Oh no! The $%&§ rust monster hit your weapon and it crumbles to rust.");
                                } else if (defender.getArmor() != null && defender.getArmor().isMetallic()) {
                                    defender.setArmor(null);
                                    return Optional.of("Friggin rust monster! It hit your armor and it crumbles to rust.");
                                }
                            }
                            return Optional.empty();
                        }
                    });
        } else if (result < 97) {
            return new Monster(
                    "Troll",
                    diceService.roll(D6, 4),
                    diceService.roll(D6, 2),
                    diceService.roll(D6, 4),
                    new Weapon("claws", 1, D10, 0, false),
                    100,
                    new CombatAction() {
                        @Override
                        public Optional<String> execute(Combatant attacker, Combatant defender, DiceService diceService) {
                            if (attacker.getHealth() < attacker.getStamina().getValue()) {
                                int regeneration = diceService.roll(D3);
                                attacker.setHealth(attacker.getHealth() + regeneration);
                                return Optional.of(format("Oh no! The troll regenerated %d points of stamina!", regeneration));
                            }
                            return Optional.empty();
                        }
                    });
        } else if (result < 100) {
            return new Monster(
                    "Vampire",
                    diceService.roll(D6, 4),
                    diceService.roll(D6, 2) + 6,
                    diceService.roll(D6, 4),
                    new Weapon("bite", 2, D4, 0, false),
                    200,
                    new CombatAction() {
                        @Override
                        public Optional<String> execute(Combatant attacker, Combatant defender, DiceService diceService) {
                            if (diceService.roll(D20) < 6) {
                                defender.getStrength().decrease();
                                return Optional.of("Don't you just hate vampires? This fella just sucked away one point of strength from you!");
                            }
                            return Optional.empty();
                        }
                    });
        } else {
            return new Monster(
                    "Dragon",
                    50,
                    18,
                    100,
                    new Weapon("claws", 2, D8, 0, false),
                    1000,
                    new CombatAction(){
                        @Override
                        public Optional<String> execute(Combatant attacker, Combatant defender, DiceService diceService) {
                            if (diceService.roll(D20) < 7) {
                                int damage = diceService.roll(D8, 5);
                                defender.decreaseCurrentStaminaBy(damage);
                                return Optional.of(format("The dragon fire is just everywhere and it's damn hot! %d of damage caused...", damage));
                            }
                            return Optional.empty();
                        }
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
            return new Weapon("magic sword", 1, DiceService.Dice.D8, 1, true);
        }
    }
}
