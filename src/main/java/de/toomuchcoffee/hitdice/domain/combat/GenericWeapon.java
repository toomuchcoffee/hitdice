package de.toomuchcoffee.hitdice.domain.combat;

import java.util.function.Supplier;

public interface GenericWeapon {
    String getName();
    Supplier<Integer> getDamage();
}