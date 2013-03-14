package ru.atott.kinoview.web.entity;

import javax.persistence.*;

@Entity
@Table(name="cinemaFilm")
public class CinemaFilm {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "filmId")
    private Long filmId;

    @Column(name = "cinemaId")
    private Long cinemaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long filmId) {
        this.filmId = filmId;
    }

    public Long getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Long cinemaId) {
        this.cinemaId = cinemaId;
    }
}
