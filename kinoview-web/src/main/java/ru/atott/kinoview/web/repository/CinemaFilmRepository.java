package ru.atott.kinoview.web.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atott.kinoview.web.entity.CinemaFilm;

@Repository
public interface CinemaFilmRepository extends CrudRepository<CinemaFilm, Long> {
    @Query("select cf from CinemaFilm cf where cf.cinemaId = ?1 and cf.filmId = ?2")
    CinemaFilm queryByCinemaAndFilm(Long cinemaId, Long filmId);
}
