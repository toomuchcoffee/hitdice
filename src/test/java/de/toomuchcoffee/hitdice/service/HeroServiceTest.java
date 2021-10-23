package de.toomuchcoffee.hitdice.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class HeroServiceTest {

    private HeroService heroService;

    @BeforeEach
    void setUp() throws Exception {
        heroService = new HeroService();
    }

    static List<Object[]> thresholds() {
        return Arrays.asList(new Object[][]{
                {0, 0},
                {1, 100},
                {2, 300},
                {3, 600},
                {4, 1000},
                {5, 1500},
                {6, 2100},
                {7, 2800},
                {8, 3600},
                {9, 4500},
                {10, 5500},
                {11, 6600},
                {12, 7800},
                {13, 9100},
                {14, 10500}
        });
    }

    @ParameterizedTest
    @MethodSource("thresholds")
    void shouldRequire100MoreForEachLevel(int level, int expectedXp) {
        int xp = heroService.xpForNextLevel(level);
        assertThat(xp).isEqualTo(expectedXp);
    }
}