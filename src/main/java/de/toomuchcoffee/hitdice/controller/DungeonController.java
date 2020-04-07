package de.toomuchcoffee.hitdice.controller;

import com.google.common.collect.ImmutableMap;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.EventType;
import de.toomuchcoffee.hitdice.domain.world.Position;
import de.toomuchcoffee.hitdice.service.DungeonService;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static de.toomuchcoffee.hitdice.domain.world.EventType.*;

@Controller
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonService dungeonService;
    private final HeroService heroService;

    @GetMapping("enter")
    public String enter(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = dungeonService.create(hero.getLevel());
        request.getSession().setAttribute("dungeon", dungeon);
        return "redirect:/dungeon";
    }

    @GetMapping("")
    public String index() {
        return "dungeon/map";
    }

    @GetMapping("{direction}")
    public String move(@PathVariable Direction direction, HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        return dungeonService.move(dungeon, direction)
                .map(event -> {
                    if (event.getEventType() == MAGIC_DOOR) {
                        return "redirect:/dungeon/enter";
                    } else {
                        request.getSession().setAttribute(event.getEventType().name().toLowerCase(), event);
                        return ImmutableMap.of(
                                MONSTER, "redirect:/combat",
                                TREASURE, "redirect:/dungeon/treasure",
                                POTION, "redirect:/dungeon/potion").get(event.getEventType());
                    }
                }).orElse("redirect:/dungeon");
    }

    @GetMapping("treasure")
    public String treasure() {
        return "dungeon/treasure";
    }

    @GetMapping("potion")
    public String potion() {
        return "dungeon/potion";
    }

    @GetMapping("clear")
    public String clear(HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        dungeonService.clear(dungeon);
        Arrays.stream(EventType.values()).forEach(e -> {
            request.getSession().removeAttribute(e.name().toLowerCase());
        });
        return "redirect:/dungeon";
    }

    @GetMapping("flee")
    public String flee(HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Position position = dungeonService.getAnyUnoccupiedPosition(dungeon);
        dungeon.setPosition(position);
        return "redirect:/dungeon";
    }

    @GetMapping("collect")
    public String collect(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Treasure treasure = (Treasure) request.getSession().getAttribute("treasure");
        heroService.collectTreasure(hero, treasure);
        return "redirect:/dungeon/clear";
    }

    @GetMapping("use")
    public String use(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Potion potion = (Potion) request.getSession().getAttribute("potion");
        heroService.drinkPotion(hero, potion);
        return "redirect:/dungeon/clear";
    }

}
