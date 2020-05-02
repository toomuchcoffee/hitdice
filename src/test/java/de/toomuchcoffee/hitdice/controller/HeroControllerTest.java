package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.HeroUpdate;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.TestData;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = HeroController.class, secure = false)
@RunWith(SpringRunner.class)
public class HeroControllerTest {

    @MockBean
    private HeroService heroService;

    @Autowired
    private MockMvc mvc;

    private Hero hero = TestData.getHero();

    @Test
    public void heroCreateStart() throws Exception {
        when(heroService.create()).thenReturn(hero);

        MockHttpSession session = new MockHttpSession();

        this.mvc.perform(get("/hero/roll")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(xpath("//h3").string("Create Hero"))
                .andExpect(xpath("//dl/dt[1]").string("Strength:"))
                .andExpect(xpath("//dl/dd[1]").string("10"))
                .andExpect(xpath("//dl/dt[2]").string("Dexterity:"))
                .andExpect(xpath("//dl/dd[2]").string("11"))
                .andExpect(xpath("//dl/dt[3]").string("Stamina:"))
                .andExpect(xpath("//dl/dd[3]").string("12"))
                .andExpect(xpath("//div[@id='hero-roll']/a[1]/@href").string("/hero/roll"))
                .andExpect(xpath("//div[@id='hero-roll']/a[2]/@href").string("/hero/confirm"))
        ;

        assertThat(session.getAttribute("hero")).isEqualToIgnoringGivenFields(hero, "combatActions");
    }

    @Test
    public void heroCreateContinue() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);

        this.mvc.perform(get("/hero/confirm")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(xpath("//h3").string("Create Hero"))
                .andExpect(xpath("//dl/dt[1]").string("Strength:"))
                .andExpect(xpath("//dl/dd[1]").string("10"))
                .andExpect(xpath("//dl/dt[2]").string("Dexterity:"))
                .andExpect(xpath("//dl/dd[2]").string("11"))
                .andExpect(xpath("//dl/dt[3]").string("Stamina:"))
                .andExpect(xpath("//dl/dd[3]").string("12"))
                .andExpect(xpath("//form/@action").string("/hero/finalize"))
                .andExpect(xpath("//form/input[@name='name']").exists())
                .andExpect(xpath("//form/input[@type='submit']/@value").string("Save"))
        ;
    }

    @Test
    public void heroCreateFinish() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("hero", hero);

        HeroUpdate update = new HeroUpdate();
        update.setName("Alrik");

        this.mvc.perform(post("/hero/finalize")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(singletonList(
                        new BasicNameValuePair("name", "Alrik")
                )))).accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("game/mode"))
        ;

        assertThat(hero.getName()).isEqualTo("Alrik");
    }
}