package tour.agency.dao.mysqldao;

import org.springframework.stereotype.Repository;
import tour.agency.dao.ConnectionManager;
import tour.agency.dao.DataAccessException;
import tour.agency.entity.Tour;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourDao {

    private final ConnectionManager connectionManager;

    public TourDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public List<Tour> findAll() {

        List<Tour> tours = new ArrayList<>();

        String sql = "SELECT * FROM tours";

        try (
                Connection con = connectionManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                tours.add(mapTour(rs));
            }

            return tours;

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public Tour findById(String id) {

        String sql = "SELECT * FROM tours WHERE id = ?";

        try (
                Connection con = connectionManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapTour(rs);
            }

            return null;

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Tour mapTour(ResultSet rs) throws Exception {

        Tour tour = new Tour();

        tour.setId(rs.getString("id"));
        tour.setTitle(rs.getString("title"));
        tour.setDestination(rs.getString("destination"));
        tour.setDescription(rs.getString("description"));
        tour.setPrice(rs.getBigDecimal("price"));
        tour.setImageUrl(rs.getString("image_url"));
        tour.setDuration(rs.getString("duration"));
        tour.setIncludedServices(rs.getString("included_services"));

        return tour;
    }
}