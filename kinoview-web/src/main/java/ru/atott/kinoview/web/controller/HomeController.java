package ru.atott.kinoview.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HomeController {
    @RequestMapping(value = "/index.html")
    public RedirectView index(HttpServletResponse response) throws IOException {
        return new RedirectView("/", true);
    }

    @RequestMapping(value = "/")
    public ModelAndView start() {
        return new ModelAndView("home/index");
    }
}
