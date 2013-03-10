package ru.atott.kinoview.web.entity;

import javax.persistence.*;

@Entity
@Table(name = "cinema")
public class Cinema {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cinemaVendorId")
    private CinemaVendor cinemaVendor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CinemaVendor getCinemaVendor() {
        return cinemaVendor;
    }

    public void setCinemaVendor(CinemaVendor cinemaVendor) {
        this.cinemaVendor = cinemaVendor;
    }
}
