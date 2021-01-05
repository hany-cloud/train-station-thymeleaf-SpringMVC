package net.hka.train.seats.booking.domain.csv.validator;

import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.exceptions.CsvValidationException;

/**
 * <p>
 * Validator that validates on the time that is provided as string using a
 * preassigned regular expression and checks if the provided time string is
 * matching a 24 time format. Then the Time24HoursValidator will invokes the
 * super {@link String#matches(String)} method on the string to be converted
 * using the preassigned regular expression string and if the two do not match
 * then a {@link CsvValidationException} will be thrown.
 * </p>
 * 
 * @author Hany Kamal
 */
public final class Time24HoursValidator extends MustMatchRegexExpression {

	/**
	 * Default constructor.
	 */
	public Time24HoursValidator() {

		/*
		 * Regex string Description
		   	(^|\s) 			# would match space or start of string using group #1
		   
		   	(				#start of group #2
			 [01]?[0-9]		#  start with 0-9,1-9,00-09,10-19
			 |				#  or
			 2[0-3]			#  start with 20-23
			)				#end of group #2
			
			[.|:]{1}		# followed by a period (.) or colon (:) exactly 1 time
			 
			[0-5][0-9]		# followed by 0..5 and 0..9, which means 00 to 59
			 
			($|\s)			# for space or end of string using group #3
		 */
		super.setParameterString("(^|\\s+)([01]?[0-9]|2[0-3])[.|:]{1}[0-5][0-9]($|\\s*)");
	}
}
