package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.controller.dto.Game;
import de.toomuchcoffee.hitdice.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GameController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class GameControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    @Test
    void listsGames() throws Exception {
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
                .andExpect(xpath("//div[@id='games']//ul/li[1]").string(containsString(dateString)))
        ;
    }
}