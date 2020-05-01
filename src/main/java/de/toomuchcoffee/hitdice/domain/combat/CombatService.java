package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.service.CombatStatus;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.toomuchcoffee.hitdice.service.CombatStatus.*;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CombatService {
    private final HeroService heroService;

    public void fight(Combat combat) {
        List<String> events = new ArrayList<>();
        if (combat.getRound() > 0) {
            events.addAll(attack(combat.getHero(), combat.getMonster()));
            events.addAll(attack(combat.getMonster(), combat.getHero()));
        }

        CombatStatus status = ONGOING;
        if (combat.getHero().isDefeated()) {
            status = DEFEAT;
        } else if (combat.getMonster().isDefeated()) {
            heroService.increaseExperience(combat.getHero(), combat.getMonster().getValue());
            status = VICTORY;
        }

        combat.nextRound();
        combat.setEvents(events);
        combat.setStatus(status);
    }

    private List<String> attack(Combatant attacker, Combatant defender) {
        return attacker.getCombatActions().stream()
                .map(a -> a.execute(attacker, defender))
                .filter(Objects::nonNull)
                .collect(toList());
    }

}
