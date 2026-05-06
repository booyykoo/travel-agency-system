package tour.agency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tour.agency.dao.ConnectionManager;
import tour.agency.dao.DAOConfig;
import tour.agency.dao.mysqldao.MySqlConnectionManager;
import tour.agency.dao.mysqldao.UserDao;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public DAOConfig daoConfig() {
		return new DAOConfig(
				"jdbc:mysql://localhost:3306/agency?useSSL=false&serverTimezone=UTC",
				"root",
				"password"
		);
	}

	@Bean
	public ConnectionManager connectionManager(DAOConfig config) {
		return new MySqlConnectionManager(config);
	}

	@Bean
	public UserDao userDao(ConnectionManager connectionManager) {
		return new UserDao(connectionManager);
	}
}
