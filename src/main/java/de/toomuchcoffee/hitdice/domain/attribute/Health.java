package de.toomuchcoffee.hitdice.domain.attribute;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Health extends AbstractAttribute {

    public Health(int value) {
        super(value);
        this.maxValue = value;
    }

    public Health(int value, int maxValue) {
        super(value);
        this.maxValue = maxValue;
    }

    public void raiseMaxValue(int value) {
        this.maxValue += value;
        this.value += value;
    }

    public boolean isInjured() {
        return value > 0 && value < maxValue;
    }

    @Override
    public String getDisplayName() {
        return String.format("%d/%d", value, maxValue);
    }

}
