package net.hka.train.seats.booking.domain;

import java.time.LocalTime;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.validators.PreAssignmentValidator;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.hka.train.seats.booking.domain.csv.converter.DepartureTimeConverter;
import net.hka.train.seats.booking.domain.csv.converter.DestinationConverter;
import net.hka.train.seats.booking.domain.csv.validator.IntegerGreaterThanZeroValidator;
import net.hka.train.seats.booking.domain.csv.validator.Time24HoursValidator;

/**
 * Represents the entity for the train trip and it holds the data that will be
 * assigned from CSV file.
 * 
 * @author Hany Kamal
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TrainTrip {

	@NonNull
	@Setter(AccessLevel.PUBLIC)
	private /* @Id */ Integer id;

	@NonNull
	@PreAssignmentValidator(validator = Time24HoursValidator.class)
	@CsvCustomBindByPosition(position = 0, required = true, converter = DepartureTimeConverter.class)
	private LocalTime departureTime;

	@NonNull
	@CsvCustomBindByPosition(position = 1, required = true, converter = DestinationConverter.class)
	private String destination;

	@NonNull
	@EqualsAndHashCode.Exclude
	@PreAssignmentValidator(validator = IntegerGreaterThanZeroValidator.class)
	// @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[0-9]{3,6}$")
	@CsvBindByPosition(position = 2, required = true)
	private Integer totalSeatsCount;
}