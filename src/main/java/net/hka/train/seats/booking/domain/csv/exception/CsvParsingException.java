package net.hka.train.seats.booking.domain.csv.exception;

import com.opencsv.exceptions.CsvException;

/**
 * Exception is thrown by a
 * {@link net.hka.train.seats.booking.domain.csv.data.provider.CsvDataProvider<E>}
 * if an error is occurred while parsing the CSV file at binding to the domain
 * entity phase.
 *
 * @author Hany Kamal
 */
public class CsvParsingException extends CsvException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CsvParsingException() {

		this("Exception is thrown while parsing the provided CSV file");
	}

	/**
	 * Constructor that allows for a human readable message.
	 *
	 * @param message Error text.
	 */
	public CsvParsingException(String message) {

		super(message);
	}
}
