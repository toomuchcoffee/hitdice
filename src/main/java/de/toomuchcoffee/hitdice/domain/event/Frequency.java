package de.toomuchcoffee.hitdice.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

@Getter
@RequiredArgsConstructor
public enum Frequency {
    COMMON(13), UNCOMMON(4), RARE(2), VERY_RARE(1);

    private final int probability;

    public static Set<Frequency> forLevel(int level, int... mods) {
        Set<Frequency> frequencies = new HashSet<>();
        int calcIndex = (int) (Math.ceil(1.0 * level / 4)) - 1;
        frequencies.add(values()[normalizeIndex(calcIndex)]);
        IntStream.of(mods).forEach(mod -> frequencies.add(values()[normalizeIndex(calcIndex + mod)]));
        return frequencies;
    }

    private static int normalizeIndex(int index) {
        return index < 0 ? 0 : (Math.min(index, values().length - 1));
    }
}
