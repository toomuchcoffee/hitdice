package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.domain.item.Treasure;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Event;
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

import static de.toomuchcoffee.hitdice.domain.world.EventType.MAGIC_DOOR;

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
        return "dungeon/explore";
    }

    @GetMapping("explore/{direction}")
    public String explore(@PathVariable Direction direction, HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Hero hero = (Hero) request.getSession().getAttribute("hero");

        Optional<Event> optEvent = dungeonService.explore(direction, dungeon);

        if (optEvent.isPresent()) {
            Event event = optEvent.get();

            if (event.getEventType() != MAGIC_DOOR) {
                request.getSession().setAttribute(event.getEventType().name().toLowerCase(), event);
            }

            switch (event.getEventType()) {
                case MONSTER: {
                    return "redirect:/combat/attack";
                }
                case TREASURE: {
                    return "dungeon/treasure";
                }
                case POTION: {
                    return "dungeon/potion";
                }
                case MAGIC_DOOR: {
                    return "redirect:/dungeon/create";
                }
                default: {
                    dungeonService.markAsVisited(dungeon);
                    return "dungeon/explore";
                }
            }
        }

        dungeonService.markAsVisited(dungeon);
        return "dungeon/explore";
    }

    @GetMapping("flee")
    public String flee(HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Position position = dungeonService.getAnyUnoccupiedPosition(dungeon);
        dungeon.setPosition(position);
        return "dungeon/explore";
    }

    @GetMapping("continue")
    public String continueExploring(HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        dungeonService.markAsVisited(dungeon);
        return "dungeon/explore";
    }

    @GetMapping("leave")
    public String leave(HttpServletRequest request) {
        request.getSession().removeAttribute("treasure");
        request.getSession().removeAttribute("potion");
        return "dungeon/explore";
    }

    @GetMapping("collect")
    public String collect(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Treasure treasure = (Treasure) request.getSession().getAttribute("treasure");
        heroService.collectTreasure(hero, treasure);
        request.getSession().removeAttribute("treasure");
        dungeonService.markAsVisited(dungeon);
        return "dungeon/explore";
    }

    @GetMapping("recover")
    public String recover(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Potion potion = (Potion) request.getSession().getAttribute("potion");
        heroService.drinkPotion(hero, potion);
        request.getSession().removeAttribute("potion");
        dungeonService.markAsVisited(dungeon);
        return "dungeon/explore";
    }

}
