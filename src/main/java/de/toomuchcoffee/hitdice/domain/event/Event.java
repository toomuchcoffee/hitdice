package de.toomuchcoffee.hitdice.domain.event;

import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Event<T> {
    private final T object;

    public String getSymbol() {
        if (object instanceof Monster) {
            return "pastafarianism";
        }
        if (object instanceof Potion) {
            return "flask";
        }
        return "coins";
    }
}
