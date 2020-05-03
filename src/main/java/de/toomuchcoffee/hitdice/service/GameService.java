package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.controller.dto.Game;
import de.toomuchcoffee.hitdice.db.HeroRepository;
import de.toomuchcoffee.hitdice.domain.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GameService {
    private final HeroRepository heroRepository;
    private final HeroMapper heroMapper;

    public List<Game> list() {
        return heroRepository.findAll().stream()
                .map(heroMapper::game)
                .collect(toList());
    }

    public void save(Hero hero) {
        heroRepository.save(heroMapper.toDb(hero));
    }

    public Hero restore(Integer id) {
        return heroRepository.findById(id)
                .map(heroMapper::fromDb)
                .orElseThrow(IllegalStateException::new);
    }

    public void delete(Integer id) {
        heroRepository.deleteById(id);
    }
}
