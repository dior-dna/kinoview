package ru.atott.kinoview.web.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.atott.kinoview.web.service.AdminService;
import ru.atott.kinoview.web.utils.Utils;

import java.util.Date;

@Component("updateDataTask")
public class UpdateDataTask {
    @Autowired
    private AdminService adminService;

    @Scheduled(cron = "0 0 0/4 * * ?")
    public void update() throws Exception {
        adminService.updateAllData(Utils.truncateDate(new Date()));
    }
}
