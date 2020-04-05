package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.HeroUpdate;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static de.toomuchcoffee.hitdice.domain.combat.Weapon.DAGGER;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Controller
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping("create")
    public String create(HttpServletRequest request) {
        Hero hero = heroService.create();
        request.getSession().setAttribute("hero", hero);
        return "hero/create-1";
    }

    @GetMapping("create/2")
    public String edit(Model model, HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        model.addAttribute("hero", hero);
        return "hero/create-2";
    }

    @PostMapping(value = "create/3", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public String save(HttpServletRequest request, HeroUpdate heroUpdate) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        hero.setName(heroUpdate.getName());
        hero.addEquipment(DAGGER);
        return "hero/create-3";
    }

}
