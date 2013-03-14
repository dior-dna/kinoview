package ru.atott.kinoview.web.utils;

import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static Date truncateDate(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }
}
