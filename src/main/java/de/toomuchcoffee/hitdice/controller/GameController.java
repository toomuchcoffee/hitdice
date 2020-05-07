package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.Game;
import de.toomuchcoffee.hitdice.domain.GameMode;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping()
    public String list(Model model) {
        List<Game> games = gameService.list();
        model.addAttribute("games", games);
        return "game/list";
    }

    @GetMapping("{id}")
    public String loadGame(@PathVariable Integer id, HttpSession session) {
        Hero hero = gameService.restore(id);
        session.setAttribute("hero", hero);
        return "redirect:/game/mode";
    }

    @GetMapping("delete/{id}")
    public String deleteGame(@PathVariable Integer id) {
        gameService.delete(id);
        return "redirect:/game";
    }

    @GetMapping("save")
    public String saveGame(@SessionAttribute Hero hero, WebRequest request) {
        gameService.save(hero);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("mode")
    public String chooseMode() {
        return "game/mode";
    }

    @GetMapping("mode/{mode}")
    public String mode(@PathVariable GameMode mode, HttpSession session) {
        session.setAttribute("mode", mode);
        return "redirect:" + mode.getUrl();
    }
}
