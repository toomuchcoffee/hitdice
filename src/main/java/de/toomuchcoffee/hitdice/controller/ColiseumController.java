package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.Combat;
import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("coliseum")
@RequiredArgsConstructor
public class ColiseumController {
    @GetMapping()
    public String list(Model model) {
        model.addAttribute("monsters", MonsterTemplate.values());
        return "coliseum";
    }

    @GetMapping("{monster}")
    public String loadMonster(@PathVariable MonsterTemplate monster, @SessionAttribute Hero hero, HttpSession session) {
        Combat combat = new Combat(hero, new Monster(monster));
        session.setAttribute("combat", combat);
        return "redirect:/combat";
    }

    @GetMapping("clear")
    public String clear(HttpSession session) {
        session.removeAttribute("combat");
        return "redirect:/coliseum";
    }

    @GetMapping("flee")
    public String flee(HttpSession session) {
        session.removeAttribute("combat");
        return "redirect:/coliseum";
    }
}
