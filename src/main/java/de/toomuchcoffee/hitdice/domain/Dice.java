package de.toomuchcoffee.hitdice.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Dice {
    public static final Dice D2 = of(1, 2);
    public static final Dice D3 = of(1, 3);
    public static final Dice D4 = of(1, 4);
    public static final Dice D6 = of(1, 6);
    public static final Dice _3D6 = of(3, 6);
    public static final Dice D8 = of(1, 8);
    public static final Dice D10 = of(1, 10);
    public static final Dice D12 = of(1, 12);
    public static final Dice D20 = of(1, 20);
    public static final Dice D100 = of(1, 100);

    private final int count;
    private final int sides;
    private final int plus;

    @EqualsAndHashCode.Exclude
    private final Random random = new Random();

    public Supplier<Integer> roller() {
        return () -> IntStream.range(0, count).map(a -> _roll()).sum() + plus;
    }

    public Integer roll() {
        return roller().get();
    }

    public boolean check(int p) {
        return roller().get() <= p;
    }

    private int _roll() {
        return random.nextInt(sides) + 1;
    }

    public String toString() {
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

    public static Dice of(int count, int sides, int plus) {
        return new Dice(count, sides, plus);
    }

    public static Dice of(int count, int sides) {
        return of(count, sides, 0);
    }

    public static Dice from(String s) {
        String regex = "(\\d+)D(\\d)([+-]\\d+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            int count = parseInt(matcher.group(1));
            int sides = parseInt(matcher.group(2));
            int plus = Optional.ofNullable(matcher.group(3)).map(Integer::parseInt).orElse(0);
            return Dice.of(count, sides, plus);
        }
        throw new IllegalArgumentException(String.format("'%s' does not match required format!", s));
    }
}
