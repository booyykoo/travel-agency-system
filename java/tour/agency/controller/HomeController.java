package tour.agency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tour.agency.service.TourService;

@Controller
public class HomeController {

    private final TourService tourService;

    public HomeController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "trips",
                tourService.getAll()
        );

        return "index";
    }

    @GetMapping("/destinations")
    public String destinations(Model model) {

        model.addAttribute(
                "trips",
                tourService.getAll()
        );

        return "index";
    }

    @GetMapping("/destinations/{id}")
    public String destinationDetails(
            @PathVariable String id,
            Model model
    ) {

        model.addAttribute(
                "tour",
                tourService.getById(id)
        );

        return "tour-details";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }
}