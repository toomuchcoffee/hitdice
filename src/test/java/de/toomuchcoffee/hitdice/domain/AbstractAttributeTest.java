package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.domain.attribute.Attribute;
import de.toomuchcoffee.hitdice.domain.attribute.Health;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AbstractAttributeTest {

    @Test
    public void shouldHavePositiveDisplayName() {
        Attribute attribute = new Attribute(18);

        assertThat(attribute.getDisplayName()).isEqualTo("18 (+3)");
    }

    @Test
    public void shouldHaveNegativeDisplayName() {
        Attribute attribute = new Attribute(6);

        assertThat(attribute.getDisplayName()).isEqualTo("6 (-1)");
    }

    @Test
    public void shouldDisplayHealthAndMaxHealth() {
        Health health = new Health(12);

        assertThat(health.getDisplayName()).isEqualTo("12/12");
    }
}