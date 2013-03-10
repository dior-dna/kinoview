package ru.atott.kinoview.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.atott.kinoview.web.entity.Cinema;
import ru.atott.kinoview.web.repository.CinemaRepository;
import ru.atott.kinoview.web.to.CinemaInfo;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CinemaService {
    @Autowired
    private CinemaRepository cinemaRepository;

    public List<CinemaInfo> getCinemas() {
        List<Cinema> cinemas = cinemaRepository.queryCinemas();
        List<CinemaInfo> infos = new ArrayList<CinemaInfo>(cinemas.size());
        for (int i = 0; i < cinemas.size(); i++) {
            Cinema cinema = cinemas.get(i);
            CinemaInfo info = new CinemaInfo();
            info.setAddress(cinema.getAddress());
            info.setCinemaId(cinema.getId());
            info.setDescription(cinema.getDescription());
            info.setName(cinema.getCinemaVendor().getName());
            info.setVendorId(cinema.getCinemaVendor().getId());
            infos.add(info);
        }
        return infos;
    }
}
