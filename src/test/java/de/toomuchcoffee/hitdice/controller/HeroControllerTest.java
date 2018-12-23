package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.HeroService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HeroController.class)
@RunWith(SpringRunner.class)
public class HeroControllerTest {

    @MockBean
    private HeroService heroService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void heroCreatePage() throws Exception {
        when(heroService.create()).thenReturn(new Hero(10, 11, 12));

        this.mvc.perform(get("/hero/create")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Hero")))
                .andExpect(content().string(containsString("<dl>\n" +
                        "    <dt>Strength</dt>\n" +
                        "    <dd>10</dd>\n" +
                        "    <dt>Dexterity</dt>\n" +
                        "    <dd>11</dd>\n" +
                        "    <dt>Stamina</dt>\n" +
                        "    <dd>12</dd>\n" +
                        "</dl>")))
                .andExpect(content().string(containsString("Roll again")))
                .andExpect(content().string(containsString("Continue")))
        ;
    }
}