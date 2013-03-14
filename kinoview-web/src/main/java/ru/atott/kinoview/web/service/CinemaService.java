package ru.atott.kinoview.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.atott.kinoview.web.entity.Cinema;
import ru.atott.kinoview.web.entity.Film;
import ru.atott.kinoview.web.repository.CinemaRepository;
import ru.atott.kinoview.web.repository.FilmRepository;
import ru.atott.kinoview.web.to.CinemaInfo;
import ru.atott.kinoview.web.to.FilmInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CinemaService {
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private FilmRepository filmRepository;

    public List<CinemaInfo> getCinemas() {
        List<Cinema> cinemas = cinemaRepository.queryCinemas();
        List<CinemaInfo> infos = new ArrayList<CinemaInfo>(cinemas.size());
        for (int i = 0; i < cinemas.size(); i++) {
            Cinema cinema = cinemas.get(i);
            CinemaInfo info = new CinemaInfo();
            info.setAddress(cinema.getAddress());
            info.setCinemaId(cinema.getId());
            info.setDescription(cinema.getDescription());
            info.setName(cinema.getCinemaVendor().getName());
            info.setVendorId(cinema.getCinemaVendor().getId());
            infos.add(info);
        }
        return infos;
    }

    public List<FilmInfo> getFilms(Date date) {
        List<Film> films = filmRepository.queryFilmsByDate(date);
        List<FilmInfo> infos = new ArrayList<FilmInfo>(films.size());
        for (int i = 0; i < films.size(); i++) {
            Film film = films.get(i);
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setTitle(film.getTitle());
            filmInfo.setActors(film.getActors());
            filmInfo.setDirector(film.getDirector());
            filmInfo.setDuration(film.getDuration());
            filmInfo.setGenre(film.getGenre());
            infos.add(filmInfo);
        }
        return infos;
    }
}
