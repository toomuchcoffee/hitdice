package de.toomuchcoffee.hitdice.domain.equipment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true) // FIXME remove
public class Shield extends Item {
    private final int defense;
}
