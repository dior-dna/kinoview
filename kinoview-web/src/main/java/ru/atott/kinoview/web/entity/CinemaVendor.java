package ru.atott.kinoview.web.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="cinemaVendor")
public class CinemaVendor {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "cinemaVendor")
    private List<Cinema> cinemas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
