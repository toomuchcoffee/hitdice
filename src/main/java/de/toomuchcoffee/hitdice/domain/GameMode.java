package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameMode {
    DUNGEON("/dungeon/enter"),
    COLISEUM("/coliseum");

    private final String url;
}
