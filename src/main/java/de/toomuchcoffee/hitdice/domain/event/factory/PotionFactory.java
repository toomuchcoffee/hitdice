package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeName;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.event.Event;
import de.toomuchcoffee.hitdice.domain.event.EventType;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;
import static de.toomuchcoffee.hitdice.domain.event.EventType.POTION;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.COMMON;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.VERY_RARE;

@Getter
@RequiredArgsConstructor
public enum PotionFactory implements EventFactory<Potion> {
    HEALTH(() -> D4.roll(2), AttributeName.HEALTH, COMMON),
    STRENGTH(D2::roll, AttributeName.STRENGTH, VERY_RARE),
    DEXTERITY(D2::roll, AttributeName.DEXTERITY, VERY_RARE),
    STAMINA(D2::roll, AttributeName.STAMINA, VERY_RARE);

    private final Supplier<Integer> potency;
    private final AttributeName type;
    private final Frequency frequency;

    private final EventType eventType = POTION;

    @Override
    public Event<Potion> createEvent() {
        return new Event<>(POTION, createObject());
    }

    @Override
    public Potion createObject() {
        return new Potion(UUID.randomUUID(), this, type.name().toLowerCase() + " potion", potency, type);
    }
}
