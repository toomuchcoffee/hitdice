package de.toomuchcoffee.hitdice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping
public class HomeController implements ErrorController {
    @GetMapping("*")
    public String home(HttpSession session, SessionStatus status) {
        status.setComplete();
        session.invalidate();
        return "home";
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.error("Request failed with http status: " + status);
        return "redirect:/";
    }

    public String getErrorPath() {
        return null;
    }
}
