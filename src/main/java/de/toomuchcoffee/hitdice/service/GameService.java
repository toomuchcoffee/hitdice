package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.db.Game;
import de.toomuchcoffee.hitdice.db.GameRepository;
import de.toomuchcoffee.hitdice.domain.HandWeapon;
import de.toomuchcoffee.hitdice.domain.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
        game.setArmor(hero.getArmor());
        game.setWeapon((HandWeapon) hero.getWeapon());
        game.setHealth(hero.getHealth());
        game.setMaxHealth(hero.getMaxHealth());
        game.setStrength(hero.getStrength().getValue());
        game.setDexterity(hero.getDexterity().getValue());
        game.setStamina(hero.getStamina().getValue());
        gameRepository.save(game);
    }

    public Hero restore(Integer gameId) {
        Game game = gameRepository.getOne(gameId);
        Hero hero = new Hero();
        hero.initializeWithPresets(game.getStrength(), game.getDexterity(), game.getStamina());
        BeanUtils.copyProperties(game, hero);
        return hero;
    }
}
