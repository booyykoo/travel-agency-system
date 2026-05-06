package tour.agency.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tour.agency.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handle(RuntimeException ex) {

        String msg = ex.getMessage();

        if (msg.equals("EMAIL_EXISTS")) {
            return ResponseEntity.status(409)
                    .body(ApiResponse.error("Email already exists"));
        }

        if (msg.equals("INVALID_CREDENTIALS")) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid credentials"));
        }

        if (msg.equals("EMAIL_NOT_VERIFIED")) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Email not verified"));
        }

        return ResponseEntity.status(400)
                .body(ApiResponse.error(msg));
    }
}