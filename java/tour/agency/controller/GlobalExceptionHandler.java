package tour.agency.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tour.agency.dao.DataAccessException;
import tour.agency.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse> handleDatabase(
            DataAccessException ex
    ) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Database error"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handle(RuntimeException ex) {

        String msg = ex.getMessage();

        if ("EMAIL_EXISTS".equals(msg)) {
            return ResponseEntity.status(409)
                    .body(ApiResponse.error("Email already exists"));
        }

        if ("INVALID_CREDENTIALS".equals(msg)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid credentials"));
        }

        if ("TOUR_NOT_FOUND".equals(msg)) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("Tour not found"));
        }

        if ("INVALID_ID".equals(msg)) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("Invalid tour id"));
        }

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(msg));
    }
}