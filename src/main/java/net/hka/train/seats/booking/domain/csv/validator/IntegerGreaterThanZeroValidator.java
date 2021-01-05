package net.hka.train.seats.booking.domain.csv.validator;

import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.exceptions.CsvValidationException;

/**
 * <p>
 * Validator that validates on the integer that is provided as string using a
 * preassigned regular expression and checks if the provided integer is
 * matching a valid integer value format with value that is greater than Zero.
 * Then the IntegerGreaterThanZeroValidator will invokes the super
 * {@link String#matches(String)} method on the string to be converted using the
 * preassigned regular expression string and if the two do not match then a
 * {@link CsvValidationException} will be thrown.
 * </p>
 * 
 * @author Hany Kamal
 */
public final class IntegerGreaterThanZeroValidator extends MustMatchRegexExpression {

	/**
	 * Default constructor.
	 */
	public IntegerGreaterThanZeroValidator() {
		// super.setParameterString("^[0]?[1-9]\\d*");
		super.setParameterString("(^|\\s+)[0]?[1-9]\\d*($|\\s*)");
	}

//	@Override
//	public boolean isValid(String value) {
//		return super.isValid(value.trim());
//	}
}
