package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping("/hero/create")
    public String create(Model model) {
        model.addAttribute("hero", heroService.create());
        return "hero/create";
    }

    @GetMapping("/hero/edit")
    public String edit(Model model) {
        model.addAttribute("hero", heroService.create());
        return "hero/create";
    }


}
