package de.toomuchcoffee.hitdice.domain.world;


import org.junit.Test;

import static de.toomuchcoffee.hitdice.domain.world.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DirectionTest {

    @Test
    public void opposite() {
        assertThat(NORTH.opposite()).isEqualTo(SOUTH);
        assertThat(EAST.opposite()).isEqualTo(WEST);
        assertThat(SOUTH.opposite()).isEqualTo(NORTH);
        assertThat(WEST.opposite()).isEqualTo(EAST);
    }
}