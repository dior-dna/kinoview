package ru.atott.kinoview.web.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atott.kinoview.web.entity.Cinema;
import ru.atott.kinoview.web.entity.Film;

import java.util.Date;
import java.util.List;

@Repository
public interface FilmRepository extends CrudRepository<Film, Long> {
    @Query("select f from Film f where lower(f.title) = ?1")
    List<Film> queryFilmByTitle(String name);

    @Query("select distinct f from Film f, CinemaFilmView cfv where f.id = cfv.filmId and cfv.date = ?1")
    List<Film> queryFilmsByDate(Date date);
}
