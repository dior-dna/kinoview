package ru.atott.kinoview.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.atott.kinoview.web.entity.Film;
import ru.atott.kinoview.web.repository.FilmRepository;
import ru.atott.kinoview.web.to.FilmInfo;
import ru.atott.kinoview.web.utils.Utils;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;

    public Film saveFilmInfo(FilmInfo filmInfo) {
        List<Film> films = filmRepository.queryFilmByNormalizedTitle(Utils.normalizeFilmTitle(filmInfo.getTitle()));
        if (films.size() > 0) {
            return films.get(0);
        } else {
            Film film = new Film();
            film.setActors(filmInfo.getActors());
            film.setDirector(filmInfo.getDirector());
            film.setDuration(filmInfo.getDuration());
            film.setGenre(filmInfo.getGenre());
            film.setTitle(filmInfo.getTitle());
            film.setNormalizedTitle(Utils.normalizeFilmTitle(filmInfo.getTitle()));
            filmRepository.save(film);
            return film;
        }
    }
}
