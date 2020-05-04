package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode // FIXME remove
public class Armor implements Item {
    private final EventFactory factory;
    private final String displayName;
    private final boolean metallic;

    private final int protection;
}
