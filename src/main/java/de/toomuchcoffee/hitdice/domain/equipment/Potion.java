package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.function.Supplier;

@Getter
@SuperBuilder
public class Potion extends Item {
    private final Supplier<Integer> potency;
    private final AttributeType type;
}
