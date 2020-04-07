package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.db.Game;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static de.toomuchcoffee.hitdice.domain.GameMode.COLISEUM;
import static de.toomuchcoffee.hitdice.domain.GameMode.DUNGEON;

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
    public String loadGame(HttpServletRequest request, @PathVariable Integer gameId) {
        Hero hero = gameService.restore(gameId);
        request.getSession().setAttribute("hero", hero);
        return "game/mode";
    }

    @GetMapping("save")
    public String saveGame(HttpServletRequest request) {
        Hero hero = (Hero) request.getSession().getAttribute("hero");
        gameService.save(hero);
        return "home";
    }

    @GetMapping("dungeon")
    public String chooseDungeon(HttpServletRequest request) {
        request.getSession().setAttribute("mode", DUNGEON);
        return "redirect:/dungeon/enter";
    }

    @GetMapping("coliseum")
    public String chooseColiseum(HttpServletRequest request) {
        request.getSession().setAttribute("mode", COLISEUM);
        return "redirect:/coliseum";
    }
}
