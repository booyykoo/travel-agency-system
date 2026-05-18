package tour.agency.service;

import tour.agency.entity.Tour;

import java.util.List;

public interface TourService {

    List<Tour> getAll();

    Tour getById(String id);
}