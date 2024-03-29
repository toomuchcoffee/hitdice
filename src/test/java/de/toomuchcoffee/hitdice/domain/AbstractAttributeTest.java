package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.attribute.Attribute;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class AbstractAttributeTest {

    @Test
    void shouldHavePositiveDisplayName() {
        Attribute attribute = new Attribute(18);

        assertThat(attribute.getDisplayName()).isEqualTo("18 (+3)");
    }

    @Test
    void shouldHaveNegativeDisplayName() {
        Attribute attribute = new Attribute(6);

        assertThat(attribute.getDisplayName()).isEqualTo("6 (-1)");
    }

    @Test
    void shouldDisplayHealthAndMaxHealth() {
        Health health = new Health(12);

        assertThat(health.getDisplayName()).isEqualTo("12/12");
    }
}