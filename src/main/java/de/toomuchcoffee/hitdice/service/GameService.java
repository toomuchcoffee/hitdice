package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.db.Game;
import de.toomuchcoffee.hitdice.db.GameRepository;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.HandWeapon;
import de.toomuchcoffee.hitdice.domain.equipment.Armor;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public List<Game> list() {
        return gameRepository.findAll();
    }

    public void save(Hero hero) {
        Game game = new Game();
        game.setName(hero.getName());
        game.setLevel(hero.getLevel());
        game.setExperience(hero.getExperience());
        game.setItems(hero.getEquipment().stream().map(Item::getName).toArray(String[]::new));
        game.setHealth(hero.getHealth().getValue());
        game.setMaxHealth(hero.getHealth().getMaxValue());
        game.setStrength(hero.getStrength().getValue());
        game.setDexterity(hero.getDexterity().getValue());
        game.setStamina(hero.getStamina().getValue());
        gameRepository.save(game);
    }

    public Hero restore(Integer gameId) {
        Game game = gameRepository.getOne(gameId);
        Hero hero = new Hero();
        hero.initializeWithPresets(game.getStrength(), game.getDexterity(), game.getStamina(), game.getHealth(), game.getMaxHealth());
        hero.setEquipment(fromStringArray(game.getItems()));
        BeanUtils.copyProperties(game, hero);
        return hero;
    }

    private List<Item> fromStringArray(String[] items) {
        if (items == null) {
            return new ArrayList<>();
        }
        return stream(items)
                .map(s -> {
                    if (stream(HandWeapon.values()).map(HandWeapon::name).anyMatch(e -> e.equals(s))) {
                        return HandWeapon.valueOf(s);
                    }
                    if (stream(Armor.values()).map(Armor::name).anyMatch(e -> e.equals(s))) {
                        return Armor.valueOf(s);
                    }
                    if (stream(Potion.values()).map(Potion::name).anyMatch(e -> e.equals(s))) {
                        return Potion.valueOf(s);
                    }
                    throw new IllegalStateException("Value doesn't match any registered enum: " + s);
                })
                .collect(toList());
    }

    public void delete(Integer gameId) {
        gameRepository.deleteById(gameId);
    }
}
