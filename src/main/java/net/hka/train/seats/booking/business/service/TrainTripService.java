package net.hka.train.seats.booking.business.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import net.hka.train.seats.booking.business.dto.TrainSeatDto;
import net.hka.train.seats.booking.business.dto.TrainTripDto;
import net.hka.train.seats.booking.domain.TrainTrip;

/**
 * Provides all necessary methods and data structures that are required for
 * handling the train trip details through the following DTO:
 * {@link net.hka.train.seats.booking.business.dto.TrainTripDto}, and
 * {@link net.hka.train.seats.booking.business.dto.TrainSeatDto}.
 * 
 * @author Hany Kamal
 */
@Service
public interface TrainTripService {

	/**
	 * Represents the default pattern value for displaying the time. 
	 */
	String DEFAULT_TIME_12HOUR_PATTERN = "hh:mm a";
	
	/**
	 * Sets the provided train trip data into a local store.
	 * 
	 * @param trips An {@link java.lang.Iterable} interface that holds of train trips to be stored.
	 */
	void setDataStore(Iterable<TrainTrip> trips);

	/**
	 * Mark the seats of the trip as booked.
	 * 
	 * @param tripDto       A trip record that holds seats to be booked.
	 * @param renderedSeats A list of last updated snapshot for seats at each view
	 *                      rendering.
	 * @return Optional of a list of
	 *         {@link net.hka.train.seats.booking.business.dto.TrainTripDto} or
	 *         empty Optional in case of booking operation is failed due to trying
	 *         to book a already booked seat.
	 */
	Optional<List<TrainSeatDto>> bookSeats(final TrainTripDto tripDto, final List<TrainSeatDto> renderedSeats);

	/**
	 * List all trips.
	 * 
	 * @return An {@Link java.lang.Iterable} interface for all trips.
	 */
	Iterable<TrainTripDto> findAll();

	/**
	 * List all trips that have the same destination as provided trip and have
	 * available seats for booking.
	 * 
	 * @param tripDto provided trip
	 * @return An {@Link java.lang.Iterable} interface for all trips that have the
	 *         same destination as provided trip and have available seats for
	 *         booking.
	 */
	Iterable<TrainTripDto> findByDestinationAvailableTrips(final TrainTripDto tripDto);

	/**
	 * List all trips that have available seats for booking.
	 * 
	 * @return An {@Link java.lang.Iterable} interface for all trips that have
	 *         available seats for booking.
	 */
	Iterable<TrainTripDto> findAllAvailableTrips();

	/**
	 * Return the corresponding trip for provided id.
	 * 
	 * @param id The id value for the targeted trip that need to be fetched.
	 * @return Optional of
	 *         {@link net.hka.train.seats.booking.business.dto.TrainTripDto} or
	 *         empty Optional in case of no record found for the provided id.
	 */
	Optional<TrainTripDto> findById(final Integer id);
}
