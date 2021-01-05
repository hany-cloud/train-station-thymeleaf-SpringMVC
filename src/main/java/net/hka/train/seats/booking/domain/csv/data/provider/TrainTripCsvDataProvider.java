package net.hka.train.seats.booking.domain.csv.data.provider;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.extern.slf4j.Slf4j;
import net.hka.train.seats.booking.domain.TrainTrip;
import net.hka.train.seats.booking.domain.csv.exception.CsvIOException;
import net.hka.train.seats.booking.domain.csv.exception.CsvNoDataFoundException;
import net.hka.train.seats.booking.domain.csv.exception.CsvParsingException;

/**
 * <p>
 * Concrete implementation for {@link CsvDataProvider} interface.
 * </p>
 * <p>
 * Automatically binding the values from the CSV file to a POJO/entity class.
 * </p>
 * <p>
 * It is recommended to add
 * <artifactId>spring-boot-configuration-processor</artifactId> to the POM file,
 * and providing the path of the CSV file that contains the train trip data
 * through defining a key "csvfile.train-trip.file-path" in
 * application.properties/yml file.
 * </p>
 * 
 * @author Hany Kamal
 */
@Component("CsvDataProvider")
@Slf4j
final class TrainTripCsvDataProvider implements CsvDataProvider<TrainTrip> {

	// Store CSV data file path from application.properties/yml.
	@Value("${csvfile.train-trip.file-path}")
	private String csvFileName; // bind csvfile.csvFile-name from properties file.

	// Holds all train trips that are loaded from CSV file, reading operation from
	// CSV file should occurred once.
	private static List<TrainTrip> trips;

	private TrainTripCsvDataProvider() {
		trips = new ArrayList<>();
	}

	@Override
	public List<TrainTrip> load() throws IllegalStateException, CsvParsingException, CsvIOException, CsvNoDataFoundException {
		if (trips == null || trips.isEmpty())
			trips = loadTrainTripsFromCSV(csvFileName);
		return trips;
	}

	// Reads and loads train trips from the CSV file.
	private List<TrainTrip> loadTrainTripsFromCSV(final String csvFileName)
			throws IllegalStateException, CsvParsingException, CsvIOException, CsvNoDataFoundException {

		Path pathToCSVFile = Paths.get(csvFileName);

		// parse CSV file to create a list of 'train trips' objects.
		try (Reader reader = Files.newBufferedReader(pathToCSVFile, StandardCharsets.US_ASCII)) {

			// create a CSV bean reader
			CsvToBean<TrainTrip> csvToBean = new CsvToBeanBuilder<TrainTrip>(reader)
					.withType(TrainTrip.class)
					.withIgnoreLeadingWhiteSpace(IGNORE_LEADING_WHITE_SPACE) // true: it is much more stable if it is
																				// handled through the converters.
					// .withSeparator(COMMA_DELIMITER)
					// .withExceptionHandler(new ExceptionHandlerThrow())
					// .withThrowExceptions(false)
					// .withIgnoreQuotations(true)
					.build();

			// iterate over the CSV bean reader to build a list of "train trips".
			try {

				int[] IdGenerators = new int[] { 0 };
				trips = csvToBean.stream().map(trip -> {
					trip.setId(++IdGenerators[0]);
					return trip;
				}).collect(Collectors.toList());

			} catch (RuntimeException re) { // Exception during parsing. Always unrecoverable.

				// build the captured error messages.
				String messages = CsvDataProvider.super.buildCapturedErrorMessages(csvToBean, csvFileName);

				// log CSV captured errors.
				log.warn(messages);

				// wrap all CSV errors inside our custom business exception class.
				throw new CsvParsingException(messages);
			}

		} catch (NoSuchFileException fne) {

			String message = MessageFormat.format("Could not find the CSV file \"{0}\".", csvFileName);

			// log IO errors.
			log.warn(message);

			// wrap IO errors inside our custom business exception class.
			throw new CsvIOException(message);

		} catch (IOException ioe) {

			ioe.printStackTrace();

			String message = MessageFormat.format(
					"Exception is thrown while attempting to read from the provided CSV file \"{0}\"\n with message: {1}.",
					csvFileName, ioe.getLocalizedMessage());

			// log IO errors.
			log.warn(message);

			// wrap IO errors inside our custom business exception class.
			throw new CsvIOException(message);
		}

		// throw an exception, if the file is empty.
		if (trips.isEmpty())
			throw new CsvNoDataFoundException("Could not find any trip!!!");

		return trips;
	}
}
