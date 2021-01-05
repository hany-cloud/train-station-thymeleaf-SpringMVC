package net.hka.train.seats.booking.domain.csv.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.opencsv.bean.AbstractBeanField;

/**
 * A custom converter to convert the departure time string to a local time
 * object.
 * 
 * @author Hany Kamal
 *
 * @param <T> {@link AbstractBeanField}.
 * @param <I> {@link AbstractBeanField}.
 */
public class DepartureTimeConverter<T, I> extends AbstractBeanField<T, I> {

	protected LocalTime convert(String csvTimeValue) {

		LocalTime localTime;
		String departureTimeValue = csvTimeValue.trim().replace('.', ':');
		try {
			localTime = LocalTime.parse(departureTimeValue, DateTimeFormatter.ofPattern("HH:mm"));
		} catch (DateTimeParseException dtpe) {
			localTime = LocalTime.parse(departureTimeValue, DateTimeFormatter.ofPattern("H:mm"));
		}

		return localTime;
	}
}
