package de.toomuchcoffee.hitdice.domain.equipment;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class Item {
    protected final boolean metallic;
    protected final String displayName;
    protected final int ordinal;
}
