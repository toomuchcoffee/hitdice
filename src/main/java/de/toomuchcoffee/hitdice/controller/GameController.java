package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.db.Game;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping()
    public String list(HttpServletRequest request) {
        List<Game> games = gameService.list();
        request.getSession().setAttribute("games", games);
        return "game";
    }

    @GetMapping("{gameId}")
    public String loadGame(HttpServletRequest request, @PathVariable Integer gameId) {
        Hero hero = gameService.restore(gameId);
        request.getSession().setAttribute("hero", hero);
        return "redirect:/dungeon/create";
    }

    @GetMapping("save")
    public String saveGame(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        gameService.save(hero);
        return "home";
    }
}
