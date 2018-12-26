package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.service.CombatService;
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

    @GetMapping("attack")
    public String enter() {
        return "redirect:/combat/attack/0";
    }

    @GetMapping("attack/{round}")
    public String attack(@PathVariable int round, Model model, HttpServletRequest request) {

        Hero hero = (Hero) request.getSession().getAttribute("hero");
        Monster monster = (Monster) request.getSession().getAttribute("monster");

        if (round > 0) {
            Integer damageCaused = combatService.attack(hero, monster);
            model.addAttribute("damageCaused", damageCaused);

            Integer damageReceived = combatService.attack(monster, hero);
            model.addAttribute("damageReceived", damageReceived);
        }

        combatService.won(hero, monster);

        model.addAttribute("round", round);
        return "/dungeon/combat";
    }

}
