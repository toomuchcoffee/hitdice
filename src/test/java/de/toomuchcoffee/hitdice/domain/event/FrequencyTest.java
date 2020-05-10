package de.toomuchcoffee.hitdice.domain.event;


import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static de.toomuchcoffee.hitdice.domain.event.Frequency.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FrequencyTest {
    @Test
    public void forHeroLevel() {
        // hero level   events of frequency
        // --------------------------------
        // 1-4          common
        // 5-8          uncommon
        // 9-12         rare
        // 13-16+       very_rare
        assertThat(forLevel(1)).isEqualTo(newHashSet(COMMON));
        assertThat(forLevel(4, 0)).isEqualTo(newHashSet(COMMON));
        assertThat(forLevel(5, 0)).isEqualTo(newHashSet(UNCOMMON));
        assertThat(forLevel(8, 0)).isEqualTo(newHashSet(UNCOMMON));
        assertThat(forLevel(9, 0)).isEqualTo(newHashSet(RARE));
        assertThat(forLevel(12, 0)).isEqualTo(newHashSet(RARE));
        assertThat(forLevel(13, 0)).isEqualTo(newHashSet(VERY_RARE));
        assertThat(forLevel(99, 0)).isEqualTo(newHashSet(VERY_RARE));
        assertThat(forLevel(4, 1)).isEqualTo(newHashSet(COMMON, UNCOMMON));
        assertThat(forLevel(8, 1)).isEqualTo(newHashSet(UNCOMMON, RARE));
        assertThat(forLevel(12, 1)).isEqualTo(newHashSet(RARE, VERY_RARE));
        assertThat(forLevel(16, 1)).isEqualTo(newHashSet(VERY_RARE));
        assertThat(forLevel(99, 1)).isEqualTo(newHashSet(VERY_RARE));
    }
}