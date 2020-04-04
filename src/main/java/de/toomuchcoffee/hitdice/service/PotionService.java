package de.toomuchcoffee.hitdice.service;

import de.toomuchcoffee.hitdice.domain.Potion;
import org.springframework.stereotype.Service;

import static de.toomuchcoffee.hitdice.domain.Potion.Type.*;
import static de.toomuchcoffee.hitdice.service.Dice.D12;
import static de.toomuchcoffee.hitdice.service.Dice.D4;

@Service
public class PotionService {

    public Potion createPotion() {
        switch (D12.roll()) {
            case 1:
                return new Potion(1, STRENGTH);
            case 2:
                return new Potion(1, STAMINA);
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            default:
                return new Potion(D4.roll(2), HEALING);
        }
    }
}