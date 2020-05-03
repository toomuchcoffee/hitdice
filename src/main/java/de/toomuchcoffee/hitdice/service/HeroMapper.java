package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.controller.dto.Game;
import de.toomuchcoffee.hitdice.db.Item;
import de.toomuchcoffee.hitdice.domain.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class HeroMapper {
    private final ItemMapper itemMapper;

    public Hero fromDb(de.toomuchcoffee.hitdice.db.Hero dbHero) {
        Hero hero = new Hero();
        hero.initialize(
                dbHero.getStrength(),
                dbHero.getDexterity(),
                dbHero.getStamina(),
                dbHero.getHealth(),
                dbHero.getMaxHealth()
        );
        hero.setEquipment(dbHero.getItems().stream()
                .map(itemMapper::fromDb)
                .collect(toList()));
        BeanUtils.copyProperties(dbHero, hero);
        return hero;
    }

    public Game game(de.toomuchcoffee.hitdice.db.Hero dbHero) {
        Game game = new Game();
        BeanUtils.copyProperties(dbHero, game);
        return game;
    }

    public de.toomuchcoffee.hitdice.db.Hero toDb(Hero hero) {
        de.toomuchcoffee.hitdice.db.Hero dbHero = new de.toomuchcoffee.hitdice.db.Hero();

        Set<Item> items = hero.getEquipment().stream()
                .map(itemMapper::toDb)
                .collect(toSet());
        items.forEach(item -> item.setHero(dbHero));
        dbHero.setItems(items);

        dbHero.setName(hero.getName());
        dbHero.setLevel(hero.getLevel());
        dbHero.setExperience(hero.getExperience());
        dbHero.setHealth(hero.getHealth().getValue());
        dbHero.setMaxHealth(hero.getHealth().getMaxValue());
        dbHero.setStrength(hero.getStrength().getValue());
        dbHero.setDexterity(hero.getDexterity().getValue());
        dbHero.setStamina(hero.getStamina().getValue());
        return dbHero;
    }


}
