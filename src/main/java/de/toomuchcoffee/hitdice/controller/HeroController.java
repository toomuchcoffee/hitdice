package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.HeroUpdate;
import de.toomuchcoffee.hitdice.controller.dto.ModalData;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.equipment.HandWeapon;
import de.toomuchcoffee.hitdice.domain.equipment.Potion;
import de.toomuchcoffee.hitdice.domain.event.factory.WeaponFactory;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.UUID;

import static de.toomuchcoffee.hitdice.domain.Dice.D4;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Controller
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping("roll")
    public String roll(HttpSession session, Model model) {
        Hero hero = heroService.create();
        session.setAttribute("hero", hero);
        model.addAttribute("confirmed", false);
        return "create";
    }

    @GetMapping("confirm")
    public String confirm(@SessionAttribute Hero hero, Model model) {
        hero.addEquipment(new HandWeapon(WeaponFactory.DAGGER, "dagger", true, D4::roll));
        model.addAttribute("hero", hero);
        model.addAttribute("confirmed", true);
        return "create";
    }

    @PostMapping(value = "finalize", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public String finalize(@SessionAttribute Hero hero, HeroUpdate heroUpdate, Model model) {
        hero.setName(heroUpdate.getName());
        return "game/mode";
    }

    @GetMapping("use/{potion}")
    public String use(@PathVariable("potion") UUID potionId, @SessionAttribute Hero hero, RedirectAttributes attributes, WebRequest request) {
        int i = hero.getEquipment().indexOf(Potion.of(potionId)); // FIXME super ugly
        Potion potion = (Potion) hero.getEquipment().remove(i);
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
