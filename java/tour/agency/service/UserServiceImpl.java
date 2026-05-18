package tour.agency.service;

import org.springframework.stereotype.Service;
import tour.agency.dao.mysqldao.UserDao;
import tour.agency.entity.User;
import tour.agency.security.PasswordUtil;
import tour.agency.security.PasswordValidator;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void register(User user) {

        if (userDao.existsByEmail(user.getEmail())) {
            throw new RuntimeException("EMAIL_EXISTS");
        }

        PasswordValidator.validate(user.getPasswordHash());

        user.setId(UUID.randomUUID().toString());
        user.setPasswordHash(PasswordUtil.hash(user.getPasswordHash()));
        user.setVerificationToken(UUID.randomUUID().toString());

        // щоб login працював без email verification
        user.setVerified(true);

        userDao.save(user);
    }

    @Override
    public User login(String email, String password) {

        User user = userDao.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("INVALID_CREDENTIALS");
        }

        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("INVALID_CREDENTIALS");
        }

        return user;
    }
}