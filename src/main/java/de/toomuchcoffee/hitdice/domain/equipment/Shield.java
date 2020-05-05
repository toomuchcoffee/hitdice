package de.toomuchcoffee.hitdice.domain.equipment;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Shield extends Item {
    private final int defense;
}
