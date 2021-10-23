package de.toomuchcoffee.hitdice.domain.event;


import org.junit.jupiter.api.Test;

import java.util.Set;

import static de.toomuchcoffee.hitdice.domain.event.Frequency.*;
import static org.assertj.core.api.Assertions.assertThat;

class FrequencyTest {

    @Test
    void forHeroLevel() {
        // hero level   events of frequency
        // --------------------------------
        // 1-4          common
        // 5-8          uncommon
        // 9-12         rare
        // 13-16+       very_rare
        assertThat(forLevel(1)).isEqualTo(Set.of(COMMON));
        assertThat(forLevel(4, 0)).isEqualTo(Set.of(COMMON));
        assertThat(forLevel(5, 0)).isEqualTo(Set.of(UNCOMMON));
        assertThat(forLevel(8, 0)).isEqualTo(Set.of(UNCOMMON));
        assertThat(forLevel(9, 0)).isEqualTo(Set.of(RARE));
        assertThat(forLevel(12, 0)).isEqualTo(Set.of(RARE));
        assertThat(forLevel(13, 0)).isEqualTo(Set.of(VERY_RARE));
        assertThat(forLevel(99, 0)).isEqualTo(Set.of(VERY_RARE));
        assertThat(forLevel(4, 1)).isEqualTo(Set.of(COMMON, UNCOMMON));
        assertThat(forLevel(8, 1)).isEqualTo(Set.of(UNCOMMON, RARE));
        assertThat(forLevel(12, 1)).isEqualTo(Set.of(RARE, VERY_RARE));
        assertThat(forLevel(16, 1)).isEqualTo(Set.of(VERY_RARE));
        assertThat(forLevel(99, 1)).isEqualTo(Set.of(VERY_RARE));
    }
}