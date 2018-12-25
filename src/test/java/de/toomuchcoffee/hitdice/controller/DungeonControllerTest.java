package de.toomuchcoffee.hitdice.controller;


import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.World;
import de.toomuchcoffee.hitdice.service.DungeonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DungeonController.class)
@RunWith(SpringRunner.class)
public class DungeonControllerTest {

    @MockBean
    private DungeonService dungeonService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void dungeonExplorePage() throws Exception {
        when(dungeonService.create()).thenReturn(new World(1));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", new Hero(10, 11, 12));

        this.mvc.perform(get("/dungeon/explore")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Explore dungeon")))
                .andExpect(content().string(containsString(
                        "<pre>" +
                        "+---+\n" +
                        "|(#)|\n" +
                        "+---+" +
                        "</pre>")))
                .andExpect(content().string(containsString("<dt>Strength</dt>")))
                .andExpect(content().string(containsString("<dd>10</dd>")))
        ;

        assertThat(session.getAttribute("hero")).isEqualToComparingFieldByField(new Hero(10, 11, 12));

    }
}