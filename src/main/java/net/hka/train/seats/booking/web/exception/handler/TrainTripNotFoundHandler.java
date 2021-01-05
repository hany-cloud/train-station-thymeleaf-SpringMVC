package net.hka.train.seats.booking.web.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import net.hka.common.web.servlet.mvc.exception.handler.MvcExceptionHandler;
import net.hka.train.seats.booking.business.exception.TrainTripNotFoundException;

/**
 * Controller advice to handle the business exception train trip not found.
 * Render the uncaught exception to the error page and return "http" status code
 * = 404 means not found.
 * 
 * @author Hany Kamal
 */
@ControllerAdvice
public final class TrainTripNotFoundHandler extends MvcExceptionHandler {

	/**
	 * Calls the super method
	 * {@link MvcExceptionHandler#handleException(Exception)}.
	 * 
	 * @param {@Link TrainTripNotFoundException}.
	 * @return {@Link ModelAndView} It holds the View which is the general error
	 *         page that is displaying the message of the thrown exception.
	 */
	@ResponseBody
	@ExceptionHandler(TrainTripNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ModelAndView taskNotFoundHandler(final TrainTripNotFoundException exception) {
		return super.handleException(exception);
	}
}
