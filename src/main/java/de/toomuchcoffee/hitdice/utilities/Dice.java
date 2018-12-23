package de.toomuchcoffee.hitdice.utilities;

import java.util.Random;

public enum Dice {
    D2(2), D3(3), D4(4), D6(6), D8(8), D10(10), D12(12), D20(20), D100(100);

    public int sides;

    Dice(int sides) {
        this.sides = sides;
    }

    private Random random = new Random();

    public int roll() {
        return random.nextInt(sides) + 1;
    }

    public int roll(int n) {
        int result = 0;
        for (int i=0; i<n; i++) {
            result += roll();
        }
        return result;
    }
}
