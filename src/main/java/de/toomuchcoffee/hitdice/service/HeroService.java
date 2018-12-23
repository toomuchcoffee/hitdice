package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.utilities.Dice;
import org.springframework.stereotype.Service;

@Service
public class HeroService {

    public Hero create() {
        return new Hero(Dice.D6.roll(3), Dice.D6.roll(3), Dice.D6.roll(3));
    }


}
