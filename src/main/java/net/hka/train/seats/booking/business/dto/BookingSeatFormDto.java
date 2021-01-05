package net.hka.train.seats.booking.business.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A POJO data transfer object for summarizing the booking seat view data model.
 * 
 * @author Hany Kamal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of") // Turns all NonNull annotation to be a validators at compile time.
public class BookingSeatFormDto {

	@NonNull
	private TrainTripDto trip;

	@NonNull
	private List<TrainSeatDto> renderedSeats; // holds a list of last updated snapshot for seats at each view rendering.
}
