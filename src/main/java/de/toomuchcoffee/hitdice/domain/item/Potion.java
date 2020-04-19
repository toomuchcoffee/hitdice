package de.toomuchcoffee.hitdice.domain.item;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeName;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;
import static de.toomuchcoffee.hitdice.domain.world.EventType.POTION;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.COMMON;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.RARE;

@RequiredArgsConstructor
@Getter
public enum Potion implements Treasure {
    HEALTH(() -> D4.roll(2), AttributeName.HEALTH, COMMON),
    STRENGTH(D2::roll, AttributeName.STRENGTH, RARE),
    DEXTERITY(D2::roll, AttributeName.DEXTERITY, RARE),
    STAMINA(D2::roll, AttributeName.STAMINA, RARE);

    private final Supplier<Integer> power;
    private final AttributeName type;
    private final Frequency frequency;

    private final EventType eventType = POTION;

    @Override
    public String getName() {
        return type.name().toLowerCase();
    }

    @Override
    public boolean isMetallic() {
        return false;
    }

    @Override
    public String getDisplayName() {
        return getName() + " potion";
    }

}
