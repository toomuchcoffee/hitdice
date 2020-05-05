package de.toomuchcoffee.hitdice.domain.equipment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true) // FIXME remove
public class Armor extends Item {
    private final int protection;
}
