package net.hka.train.seats.booking.domain.csv.data.provider;

import java.text.MessageFormat;

import com.opencsv.bean.CsvToBean;

import net.hka.train.seats.booking.domain.csv.exception.CsvIOException;
import net.hka.train.seats.booking.domain.csv.exception.CsvNoDataFoundException;
import net.hka.train.seats.booking.domain.csv.exception.CsvParsingException;

/**
 * Provides all necessary methods and data structures that are used to load CSV
 * files and binding the data to the domain entity list.
 * 
 * @author Hany Kamal
 *
 * @param <E> Type of the entity bean being populated.
 */
public interface CsvDataProvider<E> {

	/**
	 * A comma delimiter string.
	 */
	String COMMA_DELIMITER = ",";

	/**
	 * Sets to true to ignore the leading white spaces in the CSV files at assign
	 * operation for the fields that does not have Converter declared in the
	 * provided type entity using @CsvCustomBindByPosition annotation.
	 */
	boolean IGNORE_LEADING_WHITE_SPACE = true;

	/**
	 * Reads and loads train trips data from the CSV file into the provided type
	 * entity.
	 * 
	 * @return An {@link java.lang.Iterable} interface holds the data from the CSV
	 *         file.
	 * @throws @{@link CsvParsingException}
	 * @throws @{@link CsvIOException}
	 * @throws @{@link CsvNoDataFoundException}
	 */
	Iterable<E> load() throws CsvParsingException, CsvIOException, CsvNoDataFoundException;

	/**
	 * Builds the error messages from captured exceptions during CSV file parsing
	 * and assignment operations.
	 * 
	 * @param <T>         Type of the entity bean being populated.
	 * @param csvToBean   {@link CsvToBean}.
	 * @param csvFileName A string refers to the CSV file path and name.
	 * @return A string contains all captured message errors.
	 */
	public default <T> String buildCapturedErrorMessages(CsvToBean<T> csvToBean, String csvFileName) {

		StringBuilder messagesBuilder = new StringBuilder();

		// iterate over the captured exceptions to build the message errors.
		csvToBean.getCapturedExceptions().forEach((exception) -> {
			String message = MessageFormat.format(
					"\n CSV bad file format while parsing: \"{0}\"\n at line number: {1}\n returned from line: {2}\n with message: {3}.",
					csvFileName, exception.getLineNumber(), String.join(COMMA_DELIMITER, exception.getLine()),
					exception.getLocalizedMessage());

			messagesBuilder.append(message);
		});

		// return a string contains all captured message errors.
		return messagesBuilder.toString();
	}
}
