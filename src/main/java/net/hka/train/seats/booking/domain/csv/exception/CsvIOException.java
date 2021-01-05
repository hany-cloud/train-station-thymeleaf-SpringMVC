package net.hka.train.seats.booking.domain.csv.exception;

/**
 * Exception is thrown by a
 * {@link net.hka.train.seats.booking.domain.csv.data.provider.CsvDataProvider<E>}
 * if an error is occurred while reading the CSV file.
 *
 * @author Hany Kamal
 */
public class CsvIOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CsvIOException() {

		this("Exception is thrown while reading the provided CSV file");
	}

	/**
	 * Constructor that allows for a human readable message.
	 *
	 * @param message Error text.
	 */
	public CsvIOException(String message) {

		super(message);
	}

	/**
	 * Constructor that allows for re-throw the original wrapped Exception.
	 *
	 * @param e Throwable object.
	 */
	public CsvIOException(Throwable e) {

		super(e);
	}

}
