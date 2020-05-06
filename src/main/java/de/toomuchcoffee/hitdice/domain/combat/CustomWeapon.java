package de.toomuchcoffee.hitdice.domain.combat;

import de.toomuchcoffee.hitdice.domain.Dice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomWeapon implements Weapon {
    private final String displayName;
    private final Dice damage;
}
