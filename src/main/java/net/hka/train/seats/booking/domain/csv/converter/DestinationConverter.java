package net.hka.train.seats.booking.domain.csv.converter;

import org.apache.commons.text.WordUtils;

import com.opencsv.bean.AbstractBeanField;

/**
 * A custom converter to convert the destination string words into capitalized
 * words, such that the first character in each word is made up of upper case
 * character and then a series of lower case characters.
 * 
 * @author Hany Kamal
 *
 * @param <T> {@link AbstractBeanField}.
 * @param <I> {@link AbstractBeanField}.
 */
public class DestinationConverter<T, I> extends AbstractBeanField<T, I> {

	@Override
	protected String convert(String csvDestinationValue) {
		// converts the first character of the CSV destination string value to an upper
		// case and then a series of lower-case characters.
		return WordUtils.capitalizeFully(csvDestinationValue.trim());
	}
}
