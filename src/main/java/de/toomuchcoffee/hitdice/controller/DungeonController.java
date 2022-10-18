package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.ModalData;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.combat.Combat;
import de.toomuchcoffee.hitdice.domain.equipment.Item;
import de.toomuchcoffee.hitdice.domain.world.Direction;
import de.toomuchcoffee.hitdice.domain.world.Dungeon;
import de.toomuchcoffee.hitdice.domain.world.Dungeon.Tile;
import de.toomuchcoffee.hitdice.service.DungeonCreationService;
import de.toomuchcoffee.hitdice.service.DungeonExplorationService;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static de.toomuchcoffee.hitdice.domain.world.Dungeon.TileType.*;

@Controller
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonCreationService dungeonCreationService;
    private final DungeonExplorationService dungeonExplorationService;
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
            dungeon = dungeonCreationService.create(hero.getLevel());
            hero.setCurrentDungeonExplored(false);
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
        Tile tile = dungeonExplorationService.move(dungeon, direction);
        if (dungeon.isExplored() && !hero.isCurrentDungeonExplored()) {
            heroService.increaseExperience(hero, 100);
            attributes.addFlashAttribute("alert", "The dungeon level is fully explored! You gained 100XP.");
            hero.setCurrentDungeonExplored(true);
        }
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
        dungeonExplorationService.clear(dungeon);
        return "redirect:/dungeon";
    }

    @GetMapping("flee")
    public String flee(@SessionAttribute Dungeon dungeon) {
        dungeon.getAnyUnoccupiedPosition(ROOM, HALLWAY)
                .ifPresent(dungeon::setPosition);
        return "redirect:/dungeon";
    }

    @GetMapping("collect")
    public String collect(@SessionAttribute Dungeon dungeon, @SessionAttribute Hero hero) {
        dungeonExplorationService.getTreasure(dungeon).ifPresent(item -> heroService.collectTreasure(hero, item));
        return "redirect:/dungeon/clear";
    }

}
