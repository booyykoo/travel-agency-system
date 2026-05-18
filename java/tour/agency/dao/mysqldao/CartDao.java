package tour.agency.dao.mysqldao;

import org.springframework.stereotype.Repository;
import tour.agency.dao.ConnectionManager;
import tour.agency.dao.DataAccessException;
import tour.agency.entity.CartItem;
import tour.agency.entity.Tour;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartDao {

    private final ConnectionManager connectionManager;

    public CartDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void addToCart(String userId, String tourId) {

        String sql = """
                INSERT INTO cart
                (id, user_id, tour_id, quantity, created_at)
                VALUES (?, ?, ?, ?, NOW())
                """;

        try (
                Connection con = connectionManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, userId);
            ps.setString(3, tourId);
            ps.setInt(4, 1);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public List<CartItem> findByUserId(String userId) {

        List<CartItem> items = new ArrayList<>();

        String sql = """
                SELECT c.*, t.title, t.destination,
                       t.description, t.price,
                       t.image_url, t.duration
                FROM cart c
                JOIN tours t ON c.tour_id = t.id
                WHERE c.user_id = ?
                """;

        try (
                Connection con = connectionManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Tour tour = new Tour();

                tour.setId(rs.getString("tour_id"));
                tour.setTitle(rs.getString("title"));
                tour.setDestination(rs.getString("destination"));
                tour.setDescription(rs.getString("description"));
                tour.setPrice(rs.getBigDecimal("price"));
                tour.setImageUrl(rs.getString("image_url"));
                tour.setDuration(rs.getString("duration"));

                CartItem item = new CartItem();

                item.setId(rs.getString("id"));
                item.setUserId(rs.getString("user_id"));
                item.setTourId(rs.getString("tour_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setTour(tour);

                items.add(item);
            }

            return items;

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}