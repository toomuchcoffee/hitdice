package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Combat {
    private final Hero hero;
    private final Monster monster;
    private int round;
    private List<String> events;
    private CombatStatus status;

    public void nextRound() {
        this.round++;
    }
}
