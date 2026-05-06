package tour.agency.service;

import tour.agency.entity.User;

public interface UserService {

    void register(User user);
    User login(String email, String password);
}