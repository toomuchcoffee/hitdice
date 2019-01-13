package de.toomuchcoffee.hitdice.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.IntStream;

@Service
public class DiceService {
    private Random random = new Random();

    public int roll(Dice dice) {
        return random.nextInt(dice.getSides()) + 1;
    }

    public int roll(Dice dice, int n) {
        return IntStream.range(0, n).map(a -> roll(dice)).sum();
    }

    @Getter
    @RequiredArgsConstructor
    public enum Dice {
        D2(2), D3(3), D4(4), D6(6), D8(8), D10(10), D12(12), D20(20), D100(100);

        public final int sides;
    }
}
