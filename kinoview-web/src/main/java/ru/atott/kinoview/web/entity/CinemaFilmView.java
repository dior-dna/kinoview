package ru.atott.kinoview.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="cinemafilmview")
public class CinemaFilmView {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "filmId")
    private Long filmId;

    @Column(name = "cinemaId")
    private Long cinemaId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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
