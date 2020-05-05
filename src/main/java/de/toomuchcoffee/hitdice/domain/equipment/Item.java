package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class Item {
    protected final boolean metallic;
    protected final String displayName;
    protected final int ordinal;

    // FIXME should be independent from event factories
    protected final EventFactory<?> factory;
}
