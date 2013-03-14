package ru.atott.kinoview.web.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import ru.atott.kinoview.web.service.AdminService;
import ru.atott.kinoview.web.utils.Utils;

import java.util.Date;

public class UpdateDataTask {
    @Autowired
    private AdminService adminService;

    public void update() throws Exception {
        adminService.updateAllData(Utils.truncateDate(new Date()));
    }
}
