package ru.atott.kinoview.web.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atott.kinoview.web.entity.CinemaVendor;

import java.util.List;

@Repository
public interface CinemaVendorRepository extends CrudRepository<CinemaVendor, Long> {
    @Query("select cv from CinemaVendor cv order by cv.name")
    List<CinemaVendor> queryVendors();
}
