package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeName;
import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Frequency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;
import static de.toomuchcoffee.hitdice.domain.world.EventType.POTION;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.COMMON;
import static de.toomuchcoffee.hitdice.domain.world.Frequency.VERY_RARE;

@RequiredArgsConstructor
@Getter
public enum Potion implements Item {
    HEALTH(() -> D4.roll(2), AttributeName.HEALTH, COMMON),
    STRENGTH(D2::roll, AttributeName.STRENGTH, VERY_RARE),
    DEXTERITY(D2::roll, AttributeName.DEXTERITY, VERY_RARE),
    STAMINA(D2::roll, AttributeName.STAMINA, VERY_RARE);

    private final Supplier<Integer> potency;
    private final AttributeName type;
    private final Frequency frequency;

    private final EventType eventType = POTION;

    @Override
    public String getDisplayName() {
        return type.name().toLowerCase() + " potion";
    }

    @Override
    public boolean isMetallic() {
        return false;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public Event create() {
        return new Event(POTION, this);
    }
}
