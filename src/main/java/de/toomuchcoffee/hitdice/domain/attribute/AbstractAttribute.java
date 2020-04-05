package de.toomuchcoffee.hitdice.domain.attribute;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class AbstractAttribute {
    protected int value;
    protected int maxValue;

    public AbstractAttribute(int value) {
        this.value = value;
    }

    public abstract String getDisplayName();

    public void increase(int value) {
        this.value = Math.min(this.value + value, maxValue);
    }

    public void decrease(int value) {
        this.value = Math.max(this.value - value, 0);
    }
}
