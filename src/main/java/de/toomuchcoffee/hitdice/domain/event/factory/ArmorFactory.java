package de.toomuchcoffee.hitdice.domain.event.factory;

import de.toomuchcoffee.hitdice.domain.equipment.Armor;
import de.toomuchcoffee.hitdice.domain.event.Frequency;
import de.toomuchcoffee.hitdice.service.EventFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.toomuchcoffee.hitdice.domain.event.Frequency.*;

@Getter
@RequiredArgsConstructor
public enum ArmorFactory implements EventFactory<Armor> {
    PADDED("padded armor", 1, false, COMMON),
    LEATHER("leather armor", 2, false, UNCOMMON),
    CHAIN("chain mail", 3, true, RARE),
    PLATE("plate armor", 4, true, VERY_RARE);

    private final String displayName;
    private final int protection;
    private final boolean metallic;
    private final Frequency frequency;

    @Override
    public Armor create() {
        return Armor.builder()
                .displayName(displayName)
                .metallic(metallic)
                .ordinal(ordinal())
                .protection(protection)
                .build();
    }
}
