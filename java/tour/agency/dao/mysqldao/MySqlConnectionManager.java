package tour.agency.dao.mysqldao;

import tour.agency.dao.ConnectionManager;
import tour.agency.dao.DAOConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionManager implements ConnectionManager {

    private final DAOConfig config;

    public MySqlConnectionManager(DAOConfig config) {
        this.config = config;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(true);
    }

    @Override
    public Connection getConnection(boolean autoCommit) throws SQLException {
        Connection con = DriverManager.getConnection(
                config.getUrl(),
                config.getUser(),
                config.getPassword()
        );
        con.setAutoCommit(autoCommit);
        return con;
    }

    @Override
    public Connection getConnection(boolean autoCommit, int transactionIsolation) throws SQLException {
        Connection con = getConnection(autoCommit);
        con.setTransactionIsolation(transactionIsolation);
        return con;
    }
}