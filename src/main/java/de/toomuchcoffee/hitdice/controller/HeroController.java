package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.HeroUpdate;
import de.toomuchcoffee.hitdice.controller.dto.ModalData;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static de.toomuchcoffee.hitdice.domain.combat.HandWeapon.DAGGER;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Controller
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping("create")
    public String create(HttpSession session, Model model) {
        Hero hero = heroService.create();
        session.setAttribute("hero", hero);
        model.addAttribute("step", 1);
        return "create";
    }

    @GetMapping("create/2")
    public String edit(@SessionAttribute Hero hero, Model model) {
        model.addAttribute("hero", hero);
        model.addAttribute("step", 2);
        return "create";
    }

    @PostMapping(value = "create/3", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public String save(@SessionAttribute Hero hero, HeroUpdate heroUpdate, Model model) {
        hero.setName(heroUpdate.getName());
        hero.addEquipment(DAGGER);
        model.addAttribute("step", 3);
        return "create";
    }

    @GetMapping("use/{potion}")
    public String use(@PathVariable Potion potion, @SessionAttribute Hero hero, RedirectAttributes attributes, WebRequest request) {
        hero.getEquipment().remove(potion);
        heroService.drinkPotion(hero, potion);
        attributes.addFlashAttribute("modal", ModalData.forId("heroStats"));
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("stats")
    public String showStats(RedirectAttributes attributes, WebRequest request) {
        attributes.addFlashAttribute("modal", ModalData.forId("heroStats"));
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
