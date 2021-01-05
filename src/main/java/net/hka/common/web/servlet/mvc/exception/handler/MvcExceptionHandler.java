package net.hka.common.web.servlet.mvc.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Controller advice for global exception handling for any Spring MVC Web
 * application. Render any uncaught exception from any part of the system to the
 * error page.
 * </p>
 * <p>
 * The error page is set by default under spring resource resolver path as
 * "error/general", unless a property is found in a property file with key
 * "mvc.error-view-name".
 * </p>
 * 
 * @author Hany Kamal
 */
@ControllerAdvice
public class MvcExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger("Error Log");

	private static final String DEFAULT_ERROR_VIEW_NAME = "error/general";

	// Store error view name from application.properties/yml.
	@Value("${mvc.error-view-name}")
	private String errorViewName;

	/**
	 * Default constructor.
	 */
	public MvcExceptionHandler() {
		// use the default value, in case of no value is present in application.properties/yml.
		if (errorViewName == null || errorViewName.isEmpty())
			errorViewName = DEFAULT_ERROR_VIEW_NAME;
	}

	/**
	 * Handle exceptions thrown from any part of the Spring MVC Web application.
	 * 
	 * @param {@Link Exception} The exception that is thrown from any part of the
	 *               Spring MVC Web application.
	 * @return {@Link ModelAndView} It holds the View which is the general error
	 *         page that is displaying the message of the thrown exception.
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView handleException(final Exception exception) {
		
		ModelAndView modelAndView = new ModelAndView(errorViewName);
		modelAndView.addObject("errorMessage", exception.getLocalizedMessage());
		logger.warn("Handle Exception: {}", exception.getMessage());
		return modelAndView;
	}
}