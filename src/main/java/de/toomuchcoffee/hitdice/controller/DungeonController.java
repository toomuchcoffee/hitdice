package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.ModalData;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.Combat;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.domain.world.Event;
import de.toomuchcoffee.hitdice.domain.world.Position;
import de.toomuchcoffee.hitdice.service.DungeonService;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static com.google.common.collect.Sets.newHashSet;
import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.*;

@Controller
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonService dungeonService;
    private final HeroService heroService;

    @GetMapping("enter")
    public String enter(HttpSession session) {
        session.removeAttribute("dungeon");
        return "redirect:/dungeon";
    }

    @GetMapping
    public String index(HttpSession session, @SessionAttribute Hero hero) {
        Dungeon dungeon = (Dungeon) session.getAttribute("dungeon");
        if (dungeon == null) {
            dungeon = dungeonService.create(hero.getLevel());
            session.setAttribute("dungeon", dungeon);
        }
        return "dungeon/map";
    }

    @GetMapping("{direction}")
    public String move(@PathVariable Direction direction,
                       @SessionAttribute Dungeon dungeon,
                       @SessionAttribute Hero hero,
                       HttpSession session,
                       RedirectAttributes attributes) {
        Tile tile = dungeonService.move(dungeon, direction);
        if (tile.getType() == MAGIC_DOOR) {
            attributes.addFlashAttribute("modal", ModalData.magicDoorModal());
            return "redirect:/dungeon";
        } else {
            Event event = tile.getEvent();
            if (event != null) {
                switch (event.getEventType()) {
                    case MONSTER:
                        session.setAttribute("combat", new Combat(hero, (Monster) event));
                        return "redirect:/combat";
                    case POTION:
                    case TREASURE:
                        attributes.addFlashAttribute("modal", ModalData.treasureModal((Item) event));
                        attributes.addFlashAttribute("treasure", event);
                        return "redirect:/dungeon";
                    default:
                        throw new IllegalStateException("Unsupported event type: " + event.getEventType());
                }
            }
        }
        return "redirect:/dungeon";
    }

    @GetMapping("clear")
    public String clear(@SessionAttribute Dungeon dungeon) {
        dungeonService.clear(dungeon);
        return "redirect:/dungeon";
    }

    @GetMapping("flee")
    public String flee(@SessionAttribute Dungeon dungeon) {
        Position position = dungeonService.getAnyUnoccupiedPosition(dungeon, newHashSet(ROOM, HALLWAY));
        dungeon.setPosition(position);
        return "redirect:/dungeon";
    }

    @GetMapping("collect")
    public String collect(@SessionAttribute Dungeon dungeon, @SessionAttribute Hero hero) {
        dungeonService.getTreasure(dungeon).ifPresent(event -> heroService.collectTreasure(hero, event));
        return "redirect:/dungeon/clear";
    }

}
