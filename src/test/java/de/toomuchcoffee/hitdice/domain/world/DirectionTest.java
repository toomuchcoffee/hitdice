package de.toomuchcoffee.hitdice.domain.world;


import org.junit.jupiter.api.Test;

import static de.toomuchcoffee.hitdice.domain.world.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;

class DirectionTest {

    @Test
    void opposite() {
        assertThat(NORTH.opposite()).isEqualTo(SOUTH);
        assertThat(EAST.opposite()).isEqualTo(WEST);
        assertThat(SOUTH.opposite()).isEqualTo(NORTH);
        assertThat(WEST.opposite()).isEqualTo(EAST);
    }
}