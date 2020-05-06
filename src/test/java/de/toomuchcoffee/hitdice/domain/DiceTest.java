package de.toomuchcoffee.hitdice.domain;


import org.junit.Test;

import static de.toomuchcoffee.hitdice.domain.Dice.deserialize;
import static de.toomuchcoffee.hitdice.domain.Dice.of;
import static org.assertj.core.api.Assertions.assertThat;

public class DiceTest {

    @Test
    public void serializes() {
        assertThat(of(1, 10, 0).serialize()).isEqualTo("1D10");
        assertThat(of(2, 6, 0).serialize()).isEqualTo("2D6");
        assertThat(of(2, 6, 2).serialize()).isEqualTo("2D6+2");
        assertThat(of(2, 6, -1).serialize()).isEqualTo("2D6-1");
    }

    @Test
    public void deserializes() {
        assertThat(deserialize("1D10")).isEqualTo(of(1, 10, 0));
        assertThat(deserialize("2D6")).isEqualTo(of(2, 6, 0));
        assertThat(deserialize("2D6+2")).isEqualTo(of(2, 6, 2));
        assertThat(deserialize("2D6-1")).isEqualTo(of(2, 6, -1));
    }
}