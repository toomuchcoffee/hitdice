package de.toomuchcoffee.hitdice.service;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class HeroServiceTest {

    private HeroService heroService;

    private final int level;
    private final int expectedXp;

    @Before
    public void setUp() throws Exception {
        heroService = new HeroService();
    }

    @Parameters
    public static List<Object[]> balanceRates() {
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

    @Test
    public void shouldRequire100MoreForEachLevel() {
        int xp = heroService.xpForNextLevel(level);
        assertThat(xp).isEqualTo(expectedXp);
    }
}