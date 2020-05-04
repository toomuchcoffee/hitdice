package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.domain.attribute.AttributeName;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Potion implements Item {
    @EqualsAndHashCode.Include // FIXME this is ugly
    private final UUID id;
    private final EventFactory factory;
    private final String displayName;
    private final boolean metallic = false;

    private final Supplier<Integer> potency;
    private final AttributeName type;

    public static Potion of(UUID id) {
        return new Potion(id, null, null, null, null);
    }
}
