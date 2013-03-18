package ru.atott.kinoview.web.utils;

import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static Date truncateDate(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    public static String normalizeFilmTitle(String title) {
        title = title.replace('ё', 'е').toLowerCase().trim();
        return title.replaceAll("\\.|/|:|\\-|\\*|!|,|\\?|\\+|\\s", "");
    }
}
