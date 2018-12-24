package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.World;
import de.toomuchcoffee.hitdice.service.DungeonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonService dungeonService;

    @GetMapping("enter")
    public String enter(Model model, HttpServletRequest request) {
        World world = dungeonService.create();
        model.addAttribute("dungeon", world.getMap());

        Hero hero = (Hero) request.getSession().getAttribute("hero");
        model.addAttribute("hero", hero);

        return "dungeon/enter";
    }
}
