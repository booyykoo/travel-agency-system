package tour.agency.service;

import org.springframework.stereotype.Service;
import tour.agency.dao.mysqldao.TourDao;
import tour.agency.entity.Tour;

import java.util.List;

@Service
public class TourServiceImpl implements TourService {

    private final TourDao tourDao;

    public TourServiceImpl(TourDao tourDao) {
        this.tourDao = tourDao;
    }

    @Override
    public List<Tour> getAll() {
        return tourDao.findAll();
    }

    @Override
    public Tour getById(String id) {

        if (id == null || id.isBlank()) {
            throw new RuntimeException("INVALID_ID");
        }

        Tour tour = tourDao.findById(id);

        if (tour == null) {
            throw new RuntimeException("TOUR_NOT_FOUND");
        }

        return tour;
    }
}