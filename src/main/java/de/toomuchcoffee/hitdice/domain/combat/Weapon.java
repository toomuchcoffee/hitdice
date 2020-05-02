package de.toomuchcoffee.hitdice.domain.combat;

import java.util.function.Supplier;

public interface Weapon {
    String getDisplayName();
    Supplier<Integer> getDamage();
}
