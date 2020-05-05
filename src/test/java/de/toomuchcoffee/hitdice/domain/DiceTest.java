package de.toomuchcoffee.hitdice.domain;


import org.junit.Test;

import static de.toomuchcoffee.hitdice.domain.Dice.from;
import static de.toomuchcoffee.hitdice.domain.Dice.of;
import static org.assertj.core.api.Assertions.assertThat;

public class DiceTest {

    @Test
    public void writesString() {
        assertThat(of(1, 6, 0).toString()).isEqualTo("1D6");
        assertThat(of(2, 6, 0).toString()).isEqualTo("2D6");
        assertThat(of(2, 6, 2).toString()).isEqualTo("2D6+2");
        assertThat(of(2, 6, -1).toString()).isEqualTo("2D6-1");
    }

    @Test
    public void parsesString() {
        assertThat(from("1D6")).isEqualTo(of(1, 6, 0));
        assertThat(from("2D6")).isEqualTo(of(2, 6, 0));
        assertThat(from("2D6+2")).isEqualTo(of(2, 6, 2));
        assertThat(from("2D6-1")).isEqualTo(of(2, 6, -1));
    }
}