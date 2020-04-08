package de.toomuchcoffee.hitdice.domain.combat;

import java.util.function.Supplier;

public interface Weapon {
    String getName();
    Supplier<Integer> getDamage();
}
