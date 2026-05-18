package tour.agency.dao.mysqldao;


import tour.agency.dao.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlUtil {

    public static void close(AutoCloseable ac) {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }

    public static void rollback(Connection con) {
        if (con != null) {
            try {
                if (!con.getAutoCommit()) {
                    con.rollback();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
    }

    public static void commit(Connection con) {
        if (con != null) {
            try {
                if (!con.getAutoCommit()) {
                    con.commit();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
    }
}
