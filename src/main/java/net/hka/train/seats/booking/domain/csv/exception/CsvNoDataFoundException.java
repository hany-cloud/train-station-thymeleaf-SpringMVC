package net.hka.train.seats.booking.domain.csv.exception;

/**
 * Exception is thrown by a
 * {@link net.hka.train.seats.booking.domain.csv.data.provider.CsvDataProvider<E>}
 * if there is no data found in the CSV file.
 *
 * @author Hany Kamal
 */
public class CsvNoDataFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CsvNoDataFoundException() {

		this("Could not find any record");
	}

	/**
	 * Constructor that allows for a human readable message.
	 *
	 * @param message Error text.
	 */
	public CsvNoDataFoundException(String message) {

		super(message);
	}
}
