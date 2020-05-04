package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeType;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class Potion implements Item {
    private final EventFactory<Potion> factory;
    private final String displayName;
    private final boolean metallic = false;
    private final int ordinal;

    private final Supplier<Integer> potency;
    private final AttributeType type;
}
