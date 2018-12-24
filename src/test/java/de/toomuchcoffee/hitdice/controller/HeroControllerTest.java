package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.HeroUpdate;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.service.HeroService;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void heroCreateStartPage() throws Exception {
        when(heroService.create()).thenReturn(new Hero(10, 11, 12));

        MockHttpSession session = new MockHttpSession();

        this.mvc.perform(get("/hero/create")
                .session(session)
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

        assertThat(session.getAttribute("hero")).isEqualToComparingFieldByField(new Hero(10, 11, 12));
    }

    @Test
    public void heroCreateContinuePage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);

        this.mvc.perform(get("/hero/create/2")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Complete your hero")))
                .andExpect(content().string(containsString("<dl>\n" +
                        "    <dt>Strength</dt>\n" +
                        "    <dd>10</dd>\n" +
                        "    <dt>Dexterity</dt>\n" +
                        "    <dd>11</dd>\n" +
                        "    <dt>Stamina</dt>\n" +
                        "    <dd>12</dd>\n" +
                        "</dl>")))
                .andExpect(content().string(containsString("Save")))
        ;
    }

    @Test
    public void heroCreateFinishPage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);

        HeroUpdate update = new HeroUpdate();
        update.setName("Alrik");

        this.mvc.perform(post("/hero/create/3")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(singletonList(
                        new BasicNameValuePair("name", "Alrik")
                )))).accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Complete your hero")))
                .andExpect(content().string(containsString("<dl>\n" +
                        "    <dt>Name</dt>\n" +
                        "    <dd>Alrik</dd>\n" +
                        "    <dt>Strength</dt>\n" +
                        "    <dd>10</dd>\n" +
                        "    <dt>Dexterity</dt>\n" +
                        "    <dd>11</dd>\n" +
                        "    <dt>Stamina</dt>\n" +
                        "    <dd>12</dd>\n" +
                        "</dl>")))
                .andExpect(content().string(containsString("Start dungeon crawl")))
        ;

        assertThat(hero.getName()).isEqualTo("Alrik");
    }
}