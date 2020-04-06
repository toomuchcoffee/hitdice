package de.toomuchcoffee.hitdice.controller;

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

@Controller
@RequestMapping("combat")
@RequiredArgsConstructor
public class CombatController {
    private final CombatService combatService;

    @GetMapping
    public String enter() {
        return "redirect:/combat/0";
    }

    @GetMapping("{round}")
    public String attack(@PathVariable int round, Model model, HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Monster monster = (Monster) request.getSession().getAttribute("monster");

        CombatRound combatRound = combatService.fight(hero, monster, round);

        model.addAttribute("events", combatRound.getEvents());
        model.addAttribute("result", combatRound.getResult());

        model.addAttribute("monster", monster);
        model.addAttribute("round", round);

        return "dungeon/combat";
    }

}
