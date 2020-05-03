package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.controller.dto.Game;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.equipment.Armor;
import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
public class HeroMapper {
    public Hero fromDb(de.toomuchcoffee.hitdice.db.Hero dbHero) {
        Hero hero = new Hero();
        hero.initializeWithPresets(dbHero.getStrength(), dbHero.getDexterity(), dbHero.getStamina(), dbHero.getHealth(), dbHero.getMaxHealth());
        hero.setEquipment(fromStringArray(dbHero.getItems()));
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
        dbHero.setName(hero.getName());
        dbHero.setLevel(hero.getLevel());
        dbHero.setExperience(hero.getExperience());
        dbHero.setItems(hero.getEquipment().stream().map(Item::getName).toArray(String[]::new));
        dbHero.setHealth(hero.getHealth().getValue());
        dbHero.setMaxHealth(hero.getHealth().getMaxValue());
        dbHero.setStrength(hero.getStrength().getValue());
        dbHero.setDexterity(hero.getDexterity().getValue());
        dbHero.setStamina(hero.getStamina().getValue());
        return dbHero;
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

}
