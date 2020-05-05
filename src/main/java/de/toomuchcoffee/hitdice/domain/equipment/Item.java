package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode // FIXME remove
public abstract class Item {
    protected final boolean metallic;
    protected final String displayName;
    protected final int ordinal;
    protected final EventFactory<?> factory; // FIXME should not be dependent on event classes
}
