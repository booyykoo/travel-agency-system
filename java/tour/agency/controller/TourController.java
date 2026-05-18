package tour.agency.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tour.agency.entity.Tour;
import tour.agency.service.TourService;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public ResponseEntity<List<Tour>> getAll() {

        return ResponseEntity.ok(
                tourService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tour> getById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                tourService.getById(id)
        );
    }
}