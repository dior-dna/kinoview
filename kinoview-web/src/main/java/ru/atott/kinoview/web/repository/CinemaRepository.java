package ru.atott.kinoview.web.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atott.kinoview.web.entity.Cinema;
import ru.atott.kinoview.web.entity.CinemaVendor;

import java.util.List;

@Repository
public interface CinemaRepository extends CrudRepository<Cinema, Long> {
    @Query("select c from Cinema c order by c.cinemaVendor.name")
    List<Cinema> queryCinemas();
}
