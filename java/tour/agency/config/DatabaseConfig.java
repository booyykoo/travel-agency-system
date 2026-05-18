package tour.agency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tour.agency.dao.ConnectionManager;
import tour.agency.dao.DAOConfig;
import tour.agency.dao.mysqldao.CartDao;
import tour.agency.dao.mysqldao.MySqlConnectionManager;
import tour.agency.dao.mysqldao.TourDao;
import tour.agency.dao.mysqldao.UserDao;

@Configuration
public class DatabaseConfig {

    @Bean
    public DAOConfig daoConfig() {

        return new DAOConfig(
                "jdbc:mysql://localhost:3306/agency?useSSL=false&serverTimezone=UTC",
                "root",
                "Goshak0010"
        );
    }

    @Bean
    public ConnectionManager connectionManager(
            DAOConfig daoConfig
    ) {

        return new MySqlConnectionManager(daoConfig);
    }

    @Bean
    public TourDao tourDao(
            ConnectionManager connectionManager
    ) {

        return new TourDao(connectionManager);
    }

    @Bean
    public UserDao userDao(
            ConnectionManager connectionManager
    ) {

        return new UserDao(connectionManager);
    }

    @Bean
    public CartDao cartDao(
            ConnectionManager connectionManager
    ) {

        return new CartDao(connectionManager);
    }
}