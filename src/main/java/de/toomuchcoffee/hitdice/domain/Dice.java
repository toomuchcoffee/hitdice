package de.toomuchcoffee.hitdice.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Dice {
    public static final Dice D2 = Die(2);
    public static final Dice D3 = Die(3);
    public static final Dice D4 = Die(4);
    public static final Dice D6 = Die(6);
    public static final Dice _3D6 = n(3).D(6);
    public static final Dice D8 = Die(8);
    public static final Dice D10 = Die(10);
    public static final Dice D12 = Die(12);
    public static final Dice D20 = Die(20);
    public static final Dice D100 = Die(100);

    private final int count;
    private int sides;
    private int plus;

    @EqualsAndHashCode.Exclude
    private final Random random = new Random();

    public static Dice n(int n) {
        return new Dice(n);
    }

    public static Dice Die(int sides) {
        Dice dice = new Dice(1);
        dice.sides = sides;
        return dice;
    }

    public Dice D(int sides) {
        this.sides = sides;
        return this;
    }

    public Dice p(int plus) {
        this.plus = plus;
        return this;
    }


    public Integer roll() {
        return IntStream.range(0, count).map(a -> random.nextInt(sides) + 1).sum() + plus;
    }

    public boolean check(int score) {
        return roll() <= score;
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(count);
        sb.append("D");
        sb.append(sides);
        if (plus != 0) {
            if (plus > 0) {
                sb.append("+");
            }
            sb.append(plus);
        }
        return sb.toString();
    }

    public static Dice deserialize(String s) {
        String regex = "(\\d+)D(\\d+)([+-]\\d+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            int count = parseInt(matcher.group(1));
            int sides = parseInt(matcher.group(2));
            int plus = Optional.ofNullable(matcher.group(3)).map(Integer::parseInt).orElse(0);
            return n(count).D(sides).p(plus);
        }
        throw new IllegalArgumentException(String.format("'%s' does not match required format!", s));
    }
}
