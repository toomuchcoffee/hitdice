package de.toomuchcoffee.hitdice.domain.combat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class CustomWeapon implements Weapon {
    private final String name;
    private final Supplier<Integer> damage;
}
