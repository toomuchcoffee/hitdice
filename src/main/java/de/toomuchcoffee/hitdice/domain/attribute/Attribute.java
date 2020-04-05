package de.toomuchcoffee.hitdice.domain.attribute;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static java.lang.String.format;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Attribute extends AbstractAttribute {
    private int bonus;

    public Attribute(int value) {
        super(value);
        this.maxValue = 20;
        calculateBonus();
    }

    @Override
    public String getDisplayName() {
        if (bonus < 0) {
            return format("%s (%d)", value, bonus);
        } else if (bonus > 0) {
            return format("%s (+%d)", value, bonus);
        }
        return String.valueOf(value);
    }

    public void increase(int value) {
        super.increase(value);
        calculateBonus();
    }

    public void decrease(int value) {
        super.decrease(value);
        calculateBonus();
    }

    private void calculateBonus() {
        switch (value) {
            case 20:
                bonus = 5;
                break;
            case 19:
                bonus = 4;
                break;
            case 18:
                bonus = 3;
                break;
            case 17:
            case 16:
            case 15:
                bonus = 2;
                break;
            case 14:
            case 13:
                bonus = 1;
                break;
            case 8:
            case 7:
            case 6:
                bonus = -1;
                break;
            case 5:
            case 4:
                bonus = -2;
                break;
            case 3:
                bonus = -3;
                break;
            case 2:
                bonus = -4;
                break;
            case 1:
                bonus = -5;
                break;
            default:
                bonus = 0;
        }
    }

}
