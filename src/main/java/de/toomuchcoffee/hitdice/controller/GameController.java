package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.db.Game;
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

    @GetMapping("{gameId}")
    public String loadGame(@PathVariable Integer gameId, HttpSession session) {
        Hero hero = gameService.restore(gameId);
        session.setAttribute("hero", hero);
        return "game/mode";
    }

    @GetMapping("delete/{gameId}")
    public String deleteGame(@PathVariable Integer gameId) {
        gameService.delete(gameId);
        return "redirect:/game";
    }

    @GetMapping("save")
    public String saveGame(@SessionAttribute Hero hero) {
        gameService.save(hero);
        return "redirect:/dungeon";
    }

    @GetMapping("mode/{mode}")
    public String mode(@PathVariable GameMode mode, HttpSession session) {
        session.setAttribute("mode", mode);
        return "redirect:" + mode.getUrl();
    }
}
