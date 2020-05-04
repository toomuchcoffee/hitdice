package de.toomuchcoffee.hitdice.domain.equipment;

import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Shield implements Item {
    private final EventFactory factory;
    private final String displayName;
    private final boolean metallic;

    private final int defense;
}
