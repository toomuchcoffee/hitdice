package de.toomuchcoffee.hitdice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("*")
public class HomeController {
    @GetMapping
    public String home(HttpSession session, SessionStatus status) {
        status.setComplete();
        session.invalidate();
        return "home";
    }
}
