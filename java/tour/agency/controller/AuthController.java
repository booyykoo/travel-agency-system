package tour.agency.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tour.agency.dto.ApiResponse;
import tour.agency.entity.User;
import tour.agency.form.LoginForm;
import tour.agency.form.RegisterForm;
import tour.agency.security.JwtUtil;
import tour.agency.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterForm form) {

        User user = new User();
        user.setEmail(form.getEmail());
        user.setPasswordHash(form.getPassword());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setPhone(form.getPhone());

        userService.register(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Registration successful. Please verify email.",
                        user.getId()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginForm form) {

        User user = userService.login(form.getEmail(), form.getPassword());

        String token = JwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(
                ApiResponse.success("Login successful",
                        new LoginResponse(token, user))
        );
    }

    static class LoginResponse {
        public String token;
        public User user;

        public LoginResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }
    }
}