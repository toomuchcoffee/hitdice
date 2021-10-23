package de.toomuchcoffee.hitdice.controller;

import de.toomuchcoffee.hitdice.controller.dto.ModalData;
import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.Monster;
import de.toomuchcoffee.hitdice.domain.TestData;
import de.toomuchcoffee.hitdice.domain.combat.Combat;
import de.toomuchcoffee.hitdice.domain.combat.CombatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static de.toomuchcoffee.hitdice.domain.event.factory.MonsterFactory.ORC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CombatController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CombatControllerTest {
    @MockBean
    private CombatService combatService;

    @Autowired
    private MockMvc mvc;

    private Combat combat;

    @BeforeEach
    void setUp() {
        Hero hero = TestData.getHero();
        Monster monster = ORC.create();
        combat = new Combat(hero, monster);
    }

    @Test
    void combatStart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("combat", combat);

        this.mvc.perform(get("/combat")
                .secure(true)
                .session(session)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("modal", ModalData.forId("combat")));

        assertThat(session.getAttribute("combat")).isEqualTo(combat);
    }

}