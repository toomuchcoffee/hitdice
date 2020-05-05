package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;
import static de.toomuchcoffee.hitdice.domain.Dice.D4;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.COMMON;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.VERY_RARE;

@Getter
@RequiredArgsConstructor
public enum PotionFactory implements EventFactory<Potion> {
    HEALTH(() -> D4.roll(2), AttributeType.HEALTH, COMMON),
    STRENGTH(D2::roll, AttributeType.STRENGTH, VERY_RARE),
    DEXTERITY(D2::roll, AttributeType.DEXTERITY, VERY_RARE),
    STAMINA(D2::roll, AttributeType.STAMINA, VERY_RARE);

    private final Supplier<Integer> potency;
    private final AttributeType type;
    private final Frequency frequency;

    @Override
    public Potion create() {
        return Potion.builder()
                .factory(this)
                .displayName(type.name().toLowerCase() + " potion")
                .potency(potency)
                .type(type)
                .build();
    }
}
