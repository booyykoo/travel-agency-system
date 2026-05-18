package tour.agency.dao.mysqldao;

import org.springframework.stereotype.Repository;
import tour.agency.dao.ConnectionManager;
import tour.agency.dao.DataAccessException;
import tour.agency.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    private final ConnectionManager connectionManager;

    public UserDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public User findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = connectionManager.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }

            return null;

        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            SqlUtil.close(rs);
            SqlUtil.close(ps);
            SqlUtil.close(con);
        }
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    public void save(User user) {

        String sql = "INSERT INTO users " +
                "(id, email, password_hash, first_name, last_name, phone, is_verified, verification_token) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = connectionManager.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, user.getId());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getPhone());
            ps.setBoolean(7, user.isVerified());
            ps.setString(8, user.getVerificationToken());

            ps.executeUpdate();

        } catch (Exception e) {
            SqlUtil.rollback(con);
            throw new DataAccessException(e);
        } finally {
            SqlUtil.close(ps);
            SqlUtil.close(con);
        }
    }

    private User mapUser(ResultSet rs) throws Exception {

        User user = new User();

        user.setId(rs.getString("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhone(rs.getString("phone"));
        user.setVerified(rs.getBoolean("is_verified"));
        user.setVerificationToken(rs.getString("verification_token"));

        return user;
    }
}
