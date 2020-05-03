package de.toomuchcoffee.hitdice.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Game {
    private Integer id;
    private String name;
    private Integer level;
    private Date created;
}
