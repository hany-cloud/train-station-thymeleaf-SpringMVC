package net.hka.train.seats.booking.business.dto;

//import com.google.common.base.MoreObjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A POJO data transfer object for summarizing the train seat data model.
 * 
 * @author Hany Kamal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of") // Turns all NonNull annotation to be a validators at compile time.
@EqualsAndHashCode(callSuper = false)
@ToString
public class TrainSeatDto {

	@NonNull
	private /* @Id */ Integer seatNumber;

	@NonNull
	private TrainSeatType seatType;

	@NonNull
	private String localizedSeatType; // holds a display formated string for the seat type according to the
										// internationalization settings.

	private boolean seatBooked;

//	@Override
//	public String toString() {
//
//		return MoreObjects.toStringHelper(this)
//				.add("seatNumber", seatNumber)
//				.add("seatType", seatType)
//				.add("isBooked", seatBooked)
//				.toString();
//	}		
}
