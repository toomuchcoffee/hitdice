package de.toomuchcoffee.hitdice.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.stream.IntStream;

@Getter
@RequiredArgsConstructor
public enum Dice {
    D2(2), D3(3), D4(4), D6(6), D8(8), D10(10), D12(12), D20(20), D100(100);

    private Random random = new Random();
    public final int sides;

    public int roll() {
        return random.nextInt(getSides()) + 1;
    }

    public int roll(int n) {
        return IntStream.range(0, n).map(a -> roll()).sum();
    }
}
