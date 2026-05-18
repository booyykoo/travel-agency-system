package tour.agency.dto;

import jakarta.validation.constraints.NotBlank;

public class AddToCartRequest {

    @NotBlank(message = "Tour id is required")
    private String tourId;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
}