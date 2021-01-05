package net.hka.train.seats.booking.business.exception;

/**
 * Exception is thrown by a
 * {@link net.hka.train.seats.booking.web.controller.TripBookingController} if
 * no more seats are available for booking.
 *
 * @author Hany Kamal
 */
public class TrainSeatAlreadyBookedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public TrainSeatAlreadyBookedException() {

		super();
	}

	/**
	 * Constructor that allows for a human readable message.
	 *
	 * @param message Error text.
	 */
	public TrainSeatAlreadyBookedException(final String message) {

		super(message);
	}
}
