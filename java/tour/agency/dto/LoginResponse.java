package tour.agency.dto;

public class LoginResponse {

    private String token;
    private UserResponseDto user;

    public LoginResponse(
            String token,
            UserResponseDto user
    ) {

        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserResponseDto getUser() {
        return user;
    }
}