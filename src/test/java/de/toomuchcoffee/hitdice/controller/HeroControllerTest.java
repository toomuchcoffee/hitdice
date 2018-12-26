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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HeroController.class)
@RunWith(SpringRunner.class)
public class HeroControllerTest {

    @MockBean
    private HeroService heroService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void heroCreateStart() throws Exception {
        when(heroService.create()).thenReturn(new Hero(10, 11, 12));

        MockHttpSession session = new MockHttpSession();

        this.mvc.perform(get("/hero/create")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("hero/create/step-1"))
                .andExpect(xpath("//h3").string("Create Hero"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[1]").string("Strength"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[1]").string("10"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[2]").string("Dexterity"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[2]").string("11"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[3]").string("Stamina"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[3]").string("12"))
                .andExpect(xpath("//div[@id='hero-create-actions']/a[1]/@href").string("/hero/create"))
                .andExpect(xpath("//div[@id='hero-create-actions']/a[2]/@href").string("/hero/create/2"))
        ;

        assertThat(session.getAttribute("hero")).isEqualToComparingFieldByField(new Hero(10, 11, 12));
    }

    @Test
    public void heroCreateContinue() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Hero hero = new Hero(10, 11, 12);
        session.setAttribute("hero", hero);

        this.mvc.perform(get("/hero/create/2")
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("hero/create/step-2"))
                .andExpect(xpath("//h3").string("Complete your hero"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[1]").string("Strength"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[1]").string("10"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[2]").string("Dexterity"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[2]").string("11"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[3]").string("Stamina"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[3]").string("12"))
                .andExpect(xpath("//form/@action").string("/hero/create/3"))
                .andExpect(xpath("//form/input[@name='name']").exists())
                .andExpect(xpath("//form/input[@type='submit']/@value").string("Save"))
        ;
    }

    @Test
    public void heroCreateFinish() throws Exception {
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
                .andExpect(view().name("hero/create/step-3"))
                .andExpect(xpath("//h3").string("Complete your hero"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[1]").string("Name"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[1]").string("Alrik"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[2]").string("Strength"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[2]").string("10"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[3]").string("Dexterity"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[3]").string("11"))
                .andExpect(xpath("//dl[@id='hero-stats']/dt[4]").string("Stamina"))
                .andExpect(xpath("//dl[@id='hero-stats']/dd[4]").string("12"))
                .andExpect(xpath("//div[@id='hero-create-actions']/a[1]/@href").string("/dungeon/create/8"))
        ;

        assertThat(hero.getName()).isEqualTo("Alrik");
    }
}