package ru.atott.kinoview.web.vendor;

import ru.atott.kinoview.web.to.FilmInfo;

import java.util.Date;
import java.util.List;

public interface Vendor {
    List<FilmInfo> getFilms(Date date) throws Exception;
}
