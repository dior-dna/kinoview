package ru.atott.kinoview.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.atott.kinoview.web.service.CinemaService;

@Controller
public class ServiceController {
    @Autowired
    private CinemaService cinemaService;

    @ResponseBody
    @RequestMapping(value = "/service/getCinemas", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getCinemas() {
        return cinemaService.getCinemas();
    }
}
