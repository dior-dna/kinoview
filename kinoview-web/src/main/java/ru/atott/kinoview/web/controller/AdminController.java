package ru.atott.kinoview.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.atott.kinoview.web.service.AdminService;
import ru.atott.kinoview.web.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "/admin/update_luxor", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateLuxor(@RequestParam String date) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        adminService.updateLuxorData(Utils.truncateDate(dateFormat.parse(date)));
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/admin/update_kronverk", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateKronverk(@RequestParam String date) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        adminService.updateKronverkData(Utils.truncateDate(dateFormat.parse(date)));
        return true;
    }
}
