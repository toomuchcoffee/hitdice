package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.monster.AbstractMonster;
import de.toomuchcoffee.hitdice.service.CombatService;
import de.toomuchcoffee.hitdice.service.CombatService.CombatRound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.DEATH;
import static de.toomuchcoffee.hitdice.service.CombatService.CombatResult.VICTORY;

@Controller
@RequestMapping("combat")
@RequiredArgsConstructor
public class CombatController {
    private final CombatService combatService;

    @GetMapping("attack")
    public String enter() {
        return "redirect:/combat/attack/0";
    }

    @GetMapping("attack/{round}")
    public String attack(@PathVariable int round, Model model, HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        AbstractMonster monster = (AbstractMonster) request.getSession().getAttribute("monster");

        CombatRound combatRound = combatService.fight(hero, monster, round);

        model.addAttribute("events", combatRound.getEvents());

        if (combatRound.getResult() == DEATH) {
            return "dungeon/dead";
        } else if (combatRound.getResult() == VICTORY) {
            model.addAttribute("won", true);
            request.getSession().removeAttribute("monster");
        }

        model.addAttribute("monster", monster);
        model.addAttribute("round", round);

        return "dungeon/combat";
    }

}
