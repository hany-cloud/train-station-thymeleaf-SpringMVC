package net.hka.train.seats.booking.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hka.train.seats.booking.business.service.TrainTripService;
import net.hka.train.seats.booking.domain.TrainTrip;
import net.hka.train.seats.booking.domain.csv.data.provider.CsvDataProvider;

/**
 * Runs at the loading time of the application to load data from the CSV file
 * using
 * {@link net.hka.train.seats.booking.domain.csv.data.provider.CsvDataProvider}
 * and then populates/sets the data store that is handled by
 * {@link net.hka.train.seats.booking.business.service.TrainTripService}.
 * 
 * @author Hany Kamal
 */
@Configuration
@NoArgsConstructor
@Slf4j
class DataInitializer {

	@Bean
	CommandLineRunner loadCsvData(TrainTripService trainTripService, CsvDataProvider<TrainTrip> dataProvider) {
		return args -> {
			
			// loads the CSV data into a train trip entity list to map it to
			// a train trip DTO list.
			Iterable<TrainTrip> trips = dataProvider.load();


			// Sets the provided train trip data.
			trainTripService.setDataStore(trips);

			// fetching all trips, for testing purpose.
			trainTripService.findAll().forEach(trip -> log.info("Preloaded " + trip));
		};
	}
}
