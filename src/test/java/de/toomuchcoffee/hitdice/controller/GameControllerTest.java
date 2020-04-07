package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.db.Game;
import de.toomuchcoffee.hitdice.service.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GameController.class, secure = false)
@RunWith(SpringRunner.class)
public class GameControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void listsGames() throws Exception {
        String dateString = "13-01-2019 20:52";
        Date created = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString);
        Game game = new Game();
        game.setId(123);
        game.setCreated(created);
        when(gameService.list()).thenReturn(newArrayList(game));

        MockHttpSession session = new MockHttpSession();

        this.mvc.perform(get("/game")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("game/list"))
                .andExpect(xpath("//div[@id='games']//ul/li[1]").string(dateString))
        ;
    }
}