package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.ModalData;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.combat.Combat;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
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
            Object occupant = tile.getOccupant();
            if (occupant instanceof Monster) {
                session.setAttribute("combat", new Combat(hero, (Monster) occupant));
                return "redirect:/combat";
            } else if (occupant != null) {
                attributes.addFlashAttribute("modal", ModalData.treasureModal((Item) occupant));
                attributes.addFlashAttribute("treasure", occupant);
                return "redirect:/dungeon";
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
        dungeonService.getTreasure(dungeon).ifPresent(item -> heroService.collectTreasure(hero, item));
        return "redirect:/dungeon/clear";
    }

}
