package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.GameMode;
import de.toomuchcoffee.hitdice.domain.combat.Combat;
import de.toomuchcoffee.hitdice.domain.combat.CombatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("combat")
@RequiredArgsConstructor
public class CombatController {
    private final CombatService combatService;

    @GetMapping
    public String fight(@SessionAttribute Combat combat, RedirectAttributes redirectAttributes, WebRequest request) {
        combatService.fight(combat);
        redirectAttributes.addFlashAttribute("modal", "combat");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("flee")
    public String flee(HttpSession session) {
        session.removeAttribute("combat");
        GameMode mode = (GameMode) session.getAttribute("mode");
        return String.format("redirect:/%s/flee", mode.name().toLowerCase());
    }

    @GetMapping("exit")
    public String exit(HttpSession session) {
        session.removeAttribute("combat");
        GameMode mode = (GameMode) session.getAttribute("mode");
        return String.format("redirect:/%s/clear", mode.name().toLowerCase());
    }

}
