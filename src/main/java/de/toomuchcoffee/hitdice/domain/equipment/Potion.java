package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.function.Supplier;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true) // FIXME remove
public class Potion extends Item {
    private final Supplier<Integer> potency;
    private final AttributeType type;
}
