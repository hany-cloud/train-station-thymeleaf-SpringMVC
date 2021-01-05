package net.hka.train.seats.booking.business.exception;

/**
 * Exception is thrown by a
 * {@link net.hka.train.seats.booking.web.controller.TripBookingController} if
 * the trip is not found.
 *
 * @author Hany Kamal
 */
public class TrainTripNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public TrainTripNotFoundException() {

		super();
	}

	/**
	 * Constructor that allows for a human readable message.
	 *
	 * @param message Error text.
	 */
	public TrainTripNotFoundException(final String message) {

		super(message);
	}
}
