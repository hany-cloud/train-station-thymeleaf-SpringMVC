package net.hka.train.seats.booking;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import net.hka.train.seats.booking.domain.TrainTrip;

/**
 * Utility class for testing purposes.
 * 
 * @author Hany Kamal
 */
public class TestUtil {

	/**
	 * Simple implementation for reading and loading the CSV file, for test purposes.
	 * 
	 * @param csvFileFullPathName CSV file full path and name. 
	 * @return list of the train trip that is populated from the CSV file.
	 */
	public static List<TrainTrip> loadTrainTripsFromCSV(final String csvFileFullPathName) {

		List<TrainTrip> trips = new ArrayList<>();

		Path pathToCSVFile = Paths.get(csvFileFullPathName);

		// parse CSV file to create a list of 'train trips' objects.
		try (Reader reader = Files.newBufferedReader(pathToCSVFile, StandardCharsets.US_ASCII)) {

			CsvToBean<TrainTrip> csvToBean = new CsvToBeanBuilder<TrainTrip>(reader).withType(TrainTrip.class).build();

			int[] IdGenerators = new int[] { 0 };
			trips = csvToBean.stream().map(trip -> {
				trip.setId(++IdGenerators[0]);
				return trip;
			}).collect(Collectors.toList());
			
		} catch (IOException ioe) {

			ioe.printStackTrace();
		}

		return trips;
	}

	/**
	 * Returns the full path of the CSV file relative to the current class path.
	 * 
	 * @param csvFileName CSV file name.
	 * @return String represents CSV file full path and name relative to the current class path.
	 */
	public static String createCSVFullFilePath(String csvFileName) {
		Path csvFilePath = Paths.get(".", "/data/test_data/" + csvFileName);
		return csvFilePath.toAbsolutePath().toString();
	}
}
