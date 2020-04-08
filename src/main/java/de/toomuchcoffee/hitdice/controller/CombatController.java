package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.GameMode;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.service.CombatService;
import de.toomuchcoffee.hitdice.service.CombatService.CombatRound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("combat")
@RequiredArgsConstructor
public class CombatController {
    private final CombatService combatService;

    @GetMapping({"", "{round}"})
    public String fight(@PathVariable(required = false) Integer round, Model model, HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Monster monster = (Monster) request.getSession().getAttribute("monster");

        round = Optional.ofNullable(round).orElse(0);
        CombatRound combatRound = combatService.fight(hero, monster, round);

        model.addAttribute("events", combatRound.getEvents());
        model.addAttribute("result", combatRound.getResult());

        model.addAttribute("monster", monster);
        model.addAttribute("round", round);

        return "combat";
    }

    @GetMapping("flee")
    public String flee(HttpServletRequest request) {
        GameMode mode = (GameMode) request.getSession().getAttribute("mode");
        return String.format("redirect:/%s/flee", mode.name().toLowerCase());
    }

    @GetMapping("exit")
    public String exit(HttpServletRequest request) {
        GameMode mode = (GameMode) request.getSession().getAttribute("mode");
        return String.format("redirect:/%s/clear", mode.name().toLowerCase());
    }

}
