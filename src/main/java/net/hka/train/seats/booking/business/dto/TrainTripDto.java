package net.hka.train.seats.booking.business.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A POJO data transfer object for summarizing the train trip data model. It is
 * used also to hide the implementation details of train trip domain entity
 * {@link net.hka.train.seats.booking.domain.TrainTrip}.
 * 
 * @author Hany Kamal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of") // Turns all NonNull annotation to be a validators at compile time.
@EqualsAndHashCode(callSuper = false)
@ToString
public class TrainTripDto {

	@NonNull
	private Integer id;

	@NonNull
	private LocalTime departureTime;

	@NonNull
	private String localizedDepartureTime; // holds a display formated string for the departure time according to the
											// internationalization settings.

	@NonNull
	private String destination;

	@NonNull
	@EqualsAndHashCode.Exclude
	private Integer totalSeatsCount;

	@NonNull
	@EqualsAndHashCode.Exclude
	private List<TrainSeatDto> seats;

	// The following two fields are added for displaying and summarize purpose.
	// Additionally it is used to save the
	// fetching time, so no need for calculating counts at each type of fetching
	// process.
	@EqualsAndHashCode.Exclude
	private int bookedWindowSeatsCount;

	@EqualsAndHashCode.Exclude
	private int bookedAisleSeatsCount;

	/**
	 * Checks if the trip is still has available seats to be booked.
	 * 
	 * @return Boolean indicating weather the trip has a seats available for
	 *         booking.
	 */
	public boolean hasSeatAvailable() {
		return this.getBookedWindowSeatsCount() + this.getBookedAisleSeatsCount() < this.getTotalSeatsCount();
	}
}