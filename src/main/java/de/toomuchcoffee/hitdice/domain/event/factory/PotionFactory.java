package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.Dice.D2;
import static de.toomuchcoffee.hitdice.domain.Dice.n;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.COMMON;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.VERY_RARE;

@Getter
@RequiredArgsConstructor
public enum PotionFactory implements EventFactory<Potion> {
    HEALTH(n(2).D(4), AttributeType.HEALTH, COMMON),
    STRENGTH(D2, AttributeType.STRENGTH, VERY_RARE),
    DEXTERITY(D2, AttributeType.DEXTERITY, VERY_RARE),
    STAMINA(D2, AttributeType.STAMINA, VERY_RARE);

    private final Dice potency;
    private final AttributeType type;
    private final Frequency frequency;

    @Override
    public Potion create() {
        return Potion.builder()
                .displayName(type.name().toLowerCase() + " potion")
                .potency(potency)
                .type(type)
                .build();
    }
}
