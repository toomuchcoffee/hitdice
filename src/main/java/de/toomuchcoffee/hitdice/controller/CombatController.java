package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.GameMode;
import de.toomuchcoffee.hitdice.domain.combat.Combat;
import de.toomuchcoffee.hitdice.domain.combat.CombatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("combat")
@RequiredArgsConstructor
public class CombatController {
    private final CombatService combatService;

    @GetMapping
    public String fight(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Combat combat = (Combat) request.getSession().getAttribute("combat");

        combatService.fight(combat);

        redirectAttributes.addFlashAttribute("modal", "combat");

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("flee")
    public String flee(HttpServletRequest request) {
        request.getSession().removeAttribute("combat");
        GameMode mode = (GameMode) request.getSession().getAttribute("mode");
        return String.format("redirect:/%s/flee", mode.name().toLowerCase());
    }

    @GetMapping("exit")
    public String exit(HttpServletRequest request) {
        request.getSession().removeAttribute("combat");
        GameMode mode = (GameMode) request.getSession().getAttribute("mode");
        return String.format("redirect:/%s/clear", mode.name().toLowerCase());
    }

}
