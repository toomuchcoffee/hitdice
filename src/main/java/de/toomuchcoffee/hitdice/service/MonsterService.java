package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MonsterService {
    private final Random random;

    public List<MonsterTemplate> findTemplates(int heroLevel) {
        return Arrays.stream(MonsterTemplate.values())
                .filter(t -> t.getLevel() > heroLevel - 5 && t.getLevel() <= heroLevel)
                .collect(toList());
    }

    public Monster createMonster(List<MonsterTemplate> monsterTemplates) {
        int sum = monsterTemplates.stream().mapToInt(t -> t.getFrequency().getProbability()).sum();
        int roll = random.nextInt(sum);
        int p = 0;
        for (MonsterTemplate template : monsterTemplates) {
            p += template.getFrequency().getProbability();
            if (roll < p) {
                return new Monster(template);
            }
        }
        throw new IllegalStateException();
    }
}