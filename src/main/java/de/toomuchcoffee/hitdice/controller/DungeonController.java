package de.toomuchcoffee.hitdice.controller;

import com.google.common.collect.ImmutableMap;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Position;
import de.toomuchcoffee.hitdice.service.DungeonService;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static de.toomuchcoffee.hitdice.domain.world.EventType.*;

@Controller
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonService dungeonService;
    private final HeroService heroService;

    @GetMapping("create")
    public String create(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = dungeonService.create(hero.getLevel());
        request.getSession().setAttribute("dungeon", dungeon);
        return "dungeon/map";
    }

    @GetMapping({"", "{direction}"})
    public String explore(@PathVariable(required = false) Direction direction, HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        dungeonService.visited(dungeon);

        return Optional.ofNullable(direction)
                .map(d -> dungeonService.explore(d, dungeon))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(event -> {
                    if (event.getEventType() == MAGIC_DOOR) {
                        return "redirect:/dungeon/create";
                    } else {
                        request.getSession().setAttribute(event.getEventType().name().toLowerCase(), event);
                        return ImmutableMap.of(
                                MONSTER, "redirect:/combat",
                                TREASURE, "dungeon/treasure",
                                POTION, "dungeon/potion").get(event.getEventType());
                    }
                }).orElseGet(() -> {
                    request.getSession().removeAttribute("treasure");
                    request.getSession().removeAttribute("potion");
                    return "dungeon/map";
                });
    }

    @GetMapping("reenter")
    public String reenter(HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        dungeonService.cleared(dungeon);
        request.getSession().removeAttribute("monster");
        return "dungeon/map";
    }

    @GetMapping("flee")
    public String flee(HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Position position = dungeonService.getAnyUnoccupiedPosition(dungeon);
        dungeon.setPosition(position);
        return "dungeon/map";
    }

    @GetMapping("collect")
    public String collect(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Treasure treasure = (Treasure) request.getSession().getAttribute("treasure");
        heroService.collectTreasure(hero, treasure);
        request.getSession().removeAttribute("treasure");
        dungeonService.cleared(dungeon);
        return "dungeon/map";
    }

    @GetMapping("use")
    public String use(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Potion potion = (Potion) request.getSession().getAttribute("potion");
        heroService.drinkPotion(hero, potion);
        request.getSession().removeAttribute("potion");
        dungeonService.cleared(dungeon);
        return "dungeon/map";
    }

}
