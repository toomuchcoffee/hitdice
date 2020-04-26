package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import de.toomuchcoffee.hitdice.domain.world.*;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.service.DungeonService;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.google.common.collect.Sets.newHashSet;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.*;

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
        Tile tile = dungeonService.move(dungeon, direction);
        if (tile.getType() == MAGIC_DOOR) {
            return "redirect:/dungeon/enter";
        } else {
            Event event = tile.getEvent();
            if (event != null) {
                switch (event.getEventType()) {
                    case MONSTER:
                        request.getSession().setAttribute("monster", event);
                        return "redirect:/combat";
                    case POTION:
                    case TREASURE:
                        request.getSession().setAttribute("treasure", event);
                        return "redirect:/dungeon";
                    default:
                        throw new IllegalStateException("Unsupported event type: " + event.getEventType());
                }
            }
        }
        return "redirect:/dungeon";
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
        Position position = dungeonService.getAnyUnoccupiedPosition(dungeon, newHashSet(ROOM, HALLWAY));
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

}
