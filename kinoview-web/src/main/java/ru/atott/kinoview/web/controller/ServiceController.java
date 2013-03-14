package ru.atott.kinoview.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.atott.kinoview.web.service.CinemaService;
import ru.atott.kinoview.web.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ServiceController {
    @Autowired
    private CinemaService cinemaService;

    @ResponseBody
    @RequestMapping(value = "/service/getCinemas", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getCinemas() {
        return cinemaService.getCinemas();
    }

    @ResponseBody
    @RequestMapping(value = "/service/getFilms", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getFilms(@RequestParam String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date truncatedDate = Utils.truncateDate(dateFormat.parse(date));
        return cinemaService.getFilms(truncatedDate);
    }
}
