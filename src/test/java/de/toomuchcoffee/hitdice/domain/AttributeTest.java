package de.toomuchcoffee.hitdice.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AttributeTest {

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
}