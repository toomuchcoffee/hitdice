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
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
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
    public String loadGame(HttpSession session, @PathVariable Integer gameId) {
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

    @GetMapping("dungeon")
    public String chooseDungeon(HttpSession session) {
        session.setAttribute("mode", DUNGEON);
        return "redirect:/dungeon/enter";
    }

    @GetMapping("coliseum")
    public String chooseColiseum(HttpSession session) {
        session.setAttribute("mode", COLISEUM);
        return "redirect:/coliseum";
    }
}
