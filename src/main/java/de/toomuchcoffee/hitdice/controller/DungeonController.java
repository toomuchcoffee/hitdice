package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.*;
import de.toomuchcoffee.hitdice.service.DungeonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Random;

import static de.toomuchcoffee.hitdice.domain.EventType.MAGIC_DOOR;

@Controller
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonService dungeonService;

    @GetMapping("create/{size}")
    public String create(@PathVariable int size, HttpServletRequest request) {
        Dungeon dungeon = dungeonService.create(size);
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
                    int size = new Random().nextInt(hero.getLevel() + 4) + 5;
                    return String.format("redirect:/dungeon/create/%d", size);
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
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        request.getSession().removeAttribute("treasure");
        request.getSession().removeAttribute("potion");
        return "dungeon/explore";
    }

    @GetMapping("collect")
    public String collect(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Treasure treasure = (Treasure) request.getSession().getAttribute("treasure");
        dungeonService.collectTreasure(hero, treasure);
        request.getSession().removeAttribute("treasure");
        dungeonService.markAsVisited(dungeon);
        return "dungeon/explore";
    }

    @GetMapping("recover")
    public String recover(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Potion potion = (Potion) request.getSession().getAttribute("potion");
        dungeonService.drinkPotion(hero, potion);
        request.getSession().removeAttribute("potion");
        dungeonService.markAsVisited(dungeon);
        return "dungeon/explore";
    }

}
