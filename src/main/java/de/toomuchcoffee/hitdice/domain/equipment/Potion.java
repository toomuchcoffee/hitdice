package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.Dice;
import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Potion extends Item {
    private final Dice potency;
    private final AttributeType type;
}
