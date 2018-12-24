package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.HeroUpdate;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping("create")
    public String create(Model model, HttpServletRequest request) {
        Hero hero = heroService.create();
        model.addAttribute("hero", hero);
        request.getSession().setAttribute("hero", hero);
        return "hero/create/step-1";
    }

    @GetMapping("create/2")
    public String edit(Model model, HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        model.addAttribute("hero", hero);
        return "hero/create/step-2";
    }

    @PostMapping(value = "create/3", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String save(Model model, HttpServletRequest request, @ModelAttribute HeroUpdate heroUpdate) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        hero.setName(heroUpdate.getName());
        model.addAttribute("hero", hero);
        // TODO save into DB?
        return "hero/create/step-3";
    }


}
