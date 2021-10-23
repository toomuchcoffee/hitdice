package de.toomuchcoffee.hitdice.domain;


import org.junit.jupiter.api.Test;

import static de.toomuchcoffee.hitdice.domain.Dice.deserialize;
import static de.toomuchcoffee.hitdice.domain.Dice.n;
import static org.assertj.core.api.Assertions.assertThat;

class DiceTest {

    @Test
    void serializes() {
        assertThat(n(1).D(10).serialize()).isEqualTo("1D10");
        assertThat(n(2).D(6).serialize()).isEqualTo("2D6");
        assertThat(n(2).D(6).p(2).serialize()).isEqualTo("2D6+2");
        assertThat(n(2).D(6).p(-1).serialize()).isEqualTo("2D6-1");
    }

    @Test
    void deserializes() {
        assertThat(deserialize("1D10")).isEqualTo(n(1).D(10));
        assertThat(deserialize("2D6")).isEqualTo(n(2).D(6));
        assertThat(deserialize("2D6+2")).isEqualTo(n(2).D(6).p(2));
        assertThat(deserialize("2D6-1")).isEqualTo(n(2).D(6).p(-1));
    }
}