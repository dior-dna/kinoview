package ru.atott.kinoview.web.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atott.kinoview.web.entity.CinemaFilm;
import ru.atott.kinoview.web.entity.CinemaFilmView;

import java.util.Date;

@Repository
public interface CinemaFilmViewRepository extends CrudRepository<CinemaFilmView, Long> {
    @Query("select cf from CinemaFilmView cf where cf.cinemaId = ?1 and cf.filmId = ?2 and cf.date = ?3")
    CinemaFilmView queryByCinemaAndFilm(Long cinemaId, Long filmId, Date date);
}
