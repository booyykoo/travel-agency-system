package tour.agency.dto;

public class UserResponseDto {

    private String id;
    private String email;
    private String firstName;
    private String lastName;

    public UserResponseDto() {
    }

    public UserResponseDto(
            String id,
            String email,
            String firstName,
            String lastName
    ) {

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}