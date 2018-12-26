package de.toomuchcoffee.hitdice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Game {

    private Dungeon dungeon;
    private Hero hero;
    private Monster monster;
    private Date timestamp;

}
