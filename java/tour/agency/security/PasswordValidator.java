package tour.agency.security;

public class PasswordValidator {

    public static void validate(String password) {

        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new RuntimeException("Password must contain a number");
        }

        if (!password.matches(".*[!@#$%^&*].*")) {
            throw new RuntimeException("Password must contain special character");
        }
    }
}