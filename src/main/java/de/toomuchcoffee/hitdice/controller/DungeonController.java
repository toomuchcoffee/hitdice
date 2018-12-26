package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Direction;
import de.toomuchcoffee.hitdice.domain.Dungeon;
import de.toomuchcoffee.hitdice.domain.Event;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.DungeonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonService dungeonService;

    @GetMapping("create/{size}")
    public String create(@PathVariable int size, HttpServletRequest request) {
        Dungeon dungeon = dungeonService.create(size);
        request.getSession().setAttribute("dungeon", dungeon);
        return "/dungeon/explore";
    }

    @GetMapping("explore/{direction}")
    public String explore(@PathVariable Direction direction, HttpServletRequest request) {
        Dungeon dungeon = (Dungeon) request.getSession().getAttribute("dungeon");
        Hero hero = (Hero) request.getSession().getAttribute("hero");

        Event event = dungeonService.explore(direction, dungeon, hero);

        switch (event.getType()) {
            case MONSTER: {
                request.getSession().setAttribute("monster", event.getObject());
                return "redirect:/combat/attack";
            }
            case TREASURE:
            case POTION:
            case MAGIC_DOOR:
            case EMPTY:
            case EXPLORED:
            default: {
                return "/dungeon/explore";
            }
        }
    }

}
