package net.hka.train.seats.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * The entry point for the Spring MVC Web Application.
 * 
 * @author Hany Kamal
 */

// Excluding database configuration from property file, and becomes not mandatory by spring anymore.
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class TrainSeatsBookingApplication {

	/**
	 * Entry point method.
	 * 
	 * @param args An array of string arguments for the entry point method.
	 */
	public static void main(String[] args) {

		SpringApplication.run(TrainSeatsBookingApplication.class, args);
	}
}
