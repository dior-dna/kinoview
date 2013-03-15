package ru.atott.kinoview.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.atott.kinoview.web.entity.Cinema;
import ru.atott.kinoview.web.entity.CinemaFilm;
import ru.atott.kinoview.web.entity.CinemaFilmView;
import ru.atott.kinoview.web.entity.Film;
import ru.atott.kinoview.web.repository.CinemaFilmRepository;
import ru.atott.kinoview.web.repository.CinemaFilmViewRepository;
import ru.atott.kinoview.web.repository.CinemaRepository;
import ru.atott.kinoview.web.repository.FilmRepository;
import ru.atott.kinoview.web.to.FilmInfo;
import ru.atott.kinoview.web.vendor.Vendor;
import ru.atott.kinoview.web.vendor.kronverk.KronverkCinemaVendor;
import ru.atott.kinoview.web.vendor.luxor.LuxorCinemaVendor;

import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AdminService {
    @Autowired
    private FilmService filmService;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private CinemaFilmRepository cinemaFilmRepository;
    @Autowired
    private CinemaFilmViewRepository cinemaFilmViewRepository;

    public void updateAllData(Date date) throws Exception {
        updateLuxorData(date);
        updateKronverkData(date);
    }

    public void updateLuxorData(Date date) throws Exception {
        updateVendorData(cinemaRepository.findOne(3L), new LuxorCinemaVendor(LuxorCinemaVendor.LUXOR_RYAZAN_BARS_CINEMA_ID), date);
        updateVendorData(cinemaRepository.findOne(2L), new LuxorCinemaVendor(LuxorCinemaVendor.LUXOR_RYAZAN_CRUIZ_CINEMA_ID), date);
    }

    public void updateKronverkData(Date date) throws Exception {
        updateVendorData(cinemaRepository.findOne(9L), new KronverkCinemaVendor(KronverkCinemaVendor.PLAZA_CINEMA_URL, KronverkCinemaVendor.RYAZAN_CITY_ID), date);
    }

    private void updateVendorData(Cinema cinema, Vendor vendor, Date date) throws Exception {
        List<FilmInfo> films = vendor.getFilms(date);
        for (FilmInfo filmInfo: films) {
            Film film = filmService.saveFilmInfo(filmInfo);
            CinemaFilm cinemaFilm = cinemaFilmRepository.queryByCinemaAndFilm(cinema.getId(), film.getId());
            if (cinemaFilm == null) {
                cinemaFilm = new CinemaFilm();
                cinemaFilm.setCinemaId(cinema.getId());
                cinemaFilm.setFilmId(film.getId());
                cinemaFilmRepository.save(cinemaFilm);
            }
            CinemaFilmView cinemaFilmView = cinemaFilmViewRepository.queryByCinemaAndFilm(cinema.getId(), film.getId(), date);
            if (cinemaFilmView == null) {
                cinemaFilmView = new CinemaFilmView();
                cinemaFilmView.setCinemaId(cinema.getId());
                cinemaFilmView.setFilmId(film.getId());
                cinemaFilmView.setDate(date);
                cinemaFilmViewRepository.save(cinemaFilmView);
            }
        }
    }
}
