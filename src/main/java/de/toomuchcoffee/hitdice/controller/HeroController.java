package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.HeroUpdate;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.item.Potion;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static de.toomuchcoffee.hitdice.domain.combat.HandWeapon.DAGGER;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Controller
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping("create")
    public String create(Model model, HttpServletRequest request) {
        Hero hero = heroService.create();
        request.getSession().setAttribute("hero", hero);
        model.addAttribute("step", 1);
        return "create";
    }

    @GetMapping("create/2")
    public String edit(Model model, HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        model.addAttribute("hero", hero);
        model.addAttribute("step", 2);
        return "create";
    }

    @PostMapping(value = "create/3", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public String save(Model model, HttpServletRequest request, HeroUpdate heroUpdate) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        hero.setName(heroUpdate.getName());
        hero.addEquipment(DAGGER);
        model.addAttribute("step", 3);
        return "create";
    }

    @GetMapping("use/{potion}")
    public String use(HttpServletRequest request, @PathVariable Potion potion, RedirectAttributes attributes) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        hero.getEquipment().remove(potion);
        heroService.drinkPotion(hero, potion);
        attributes.addFlashAttribute("modal", "heroStats");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("stats")
    public String showStats(HttpServletRequest request, RedirectAttributes attributes) {
        String referer = request.getHeader("Referer");
        attributes.addFlashAttribute("modal", "heroStats");
        return "redirect:" + referer;
    }

}
