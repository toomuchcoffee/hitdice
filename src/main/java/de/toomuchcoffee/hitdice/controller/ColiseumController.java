package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.monster.Monster;
import de.toomuchcoffee.hitdice.domain.monster.MonsterTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("coliseum")
@RequiredArgsConstructor
public class ColiseumController {
    @GetMapping()
    public String list(HttpServletRequest request) {
        request.setAttribute("monsters", MonsterTemplate.values());
        return "coliseum";
    }

    @GetMapping("{monster}")
    public String loadMonster(HttpServletRequest request, @PathVariable MonsterTemplate monster) {
        request.getSession().setAttribute("monster", new Monster(monster));
        return "redirect:/combat";
    }

    @GetMapping("clear")
    public String clear(HttpServletRequest request) {
        request.getSession().removeAttribute("monster");
        return "redirect:/coliseum";
    }

    @GetMapping("flee")
    public String flee(HttpServletRequest request) {
        request.getSession().removeAttribute("monster");
        return "redirect:/coliseum";
    }
}
