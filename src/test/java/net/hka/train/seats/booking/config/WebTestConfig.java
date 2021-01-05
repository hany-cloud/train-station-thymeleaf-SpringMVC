package net.hka.train.seats.booking.config;

import java.util.Locale;
import java.util.Properties;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import net.hka.train.seats.booking.business.exception.TrainTripNotFoundException;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.web.servlet.view.JstlView;

/**
 * This class provides static factory methods which are used
 * to create and configure the Spring MVC infrastructure components
 * which we can use when we write unit tests for "normal" Spring MVC
 * controllers.
 */
@ActiveProfiles("test")
public final class WebTestConfig {

    private static final String HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR = "500";
    private static final String HTTP_STATUS_CODE_NOT_FOUND = "404";

//    private static final String VIEW_BASE_PATH = "/WEB-INF/jsp/";
//    private static final String VIEW_FILENAME_SUFFIX = ".jsp";
    private static final String VIEW_NAME_ERROR_VIEW = "error/general";
    private static final String VIEW_NAME_NOT_FOUND_VIEW = "error/general";//"error/404";

    public static final Locale LOCALE = Locale.ENGLISH;

    /**
     * Prevents instantiation.
     */
    private WebTestConfig() {}

    /**
     * This method returns an exception resolver that maps exceptions to the error view names and
     * resolves the returned HTTP status code of the rendered error view.
     *
     * @return {@link org.springframework.web.servlet.handler.SimpleMappingExceptionResolver}
     */
    public static SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();

        //exceptionMappings.put("net.hka.train.seats.booking.business.exception.TrainTripNotFoundException", VIEW_NAME_NOT_FOUND_VIEW);
        exceptionMappings.put("java.lang.RuntimeException", VIEW_NAME_ERROR_VIEW);
        exceptionMappings.put("java.lang.Exception", VIEW_NAME_ERROR_VIEW);
        
        exceptionResolver.setExceptionMappings(exceptionMappings);
        exceptionResolver.setExcludedExceptions(TrainTripNotFoundException.class);
        
        Properties statusCodes = new Properties();

        statusCodes.put(VIEW_NAME_NOT_FOUND_VIEW, HTTP_STATUS_CODE_NOT_FOUND);
        statusCodes.put(VIEW_NAME_ERROR_VIEW, HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR);

        exceptionResolver.setStatusCodes(statusCodes);

        return exceptionResolver;
    }

    /**
     * This method creates a locale resolver that always returns {@code Locale.ENGLISH}
     * 
     * @return {@link LocaleResolver}
     */
    public static LocaleResolver fixedLocaleResolver() {
        return new FixedLocaleResolver(LOCALE);
    }
}

