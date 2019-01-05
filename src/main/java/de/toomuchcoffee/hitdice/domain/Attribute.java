package de.toomuchcoffee.hitdice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static java.lang.String.format;

@Getter
@EqualsAndHashCode
public class Attribute {
    private int value;
    private int bonus;

    public Attribute(int value) {
        this.value = value;
        calculateBonus();
    }

    public String getDisplayName() {
        if (bonus < 0) {
            return format("%s (%d)", value, bonus);
        } else if (bonus > 0) {
            return format("%s (+%d)", value, bonus);
        }
        return String.valueOf(value);
    }

    public void increase() {
        value++;
        calculateBonus();
    }

    public void decrease() {
        value--;
        calculateBonus();
    }

    private void calculateBonus() {
        if (value >= 18) {
            bonus = 3;
        } else if (value >= 16) {
            bonus = 2;
        } else if (value >= 13) {
            bonus = 1;
        } else if (value < 9) {
            bonus = -1;
        } else {
            bonus = 0;
        }
    }

}
