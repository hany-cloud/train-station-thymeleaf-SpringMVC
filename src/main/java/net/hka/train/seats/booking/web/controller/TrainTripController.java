package net.hka.train.seats.booking.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.hka.common.web.servlet.util.AjaxUtils;
import net.hka.train.seats.booking.business.dto.BookingSeatFormDto;
import net.hka.train.seats.booking.business.dto.TrainSeatDto;
import net.hka.train.seats.booking.business.dto.TrainTripDto;
import net.hka.train.seats.booking.business.exception.TrainSeatAlreadyBookedException;
import net.hka.train.seats.booking.business.exception.TrainTripNotFoundException;
import net.hka.train.seats.booking.business.service.TrainTripService;

/**
 * The end point MVC controller that manages all the requests for handling the train trip operations.
 * 
 * @author Hany Kamal
 */
@Controller
class TrainTripController {

	// Views names and relative path.
	private static final String TRIP_LIST_VIEW_NAME = "trip/trip-list";
	private static final String TRIP_DETAILS_VIEW_NAME = "trip/trip-details";
	private static final String TRIP_BOOK_SEAT_FORM_VIEW_NAME = "trip/book-form";

	// Fragments - for AJAX rendering.
	// private static final String TRIP_LIST_FRAGMENT = "tripListTableFragment";
	// private static final String TRIP_FORM_FRAGMENT = "tripFormFragment";
	private static final String AJAX_RENDER_BOOK_FORM_FRAGMENT = "ajaxRenderFragment";

	// get error messages from message resource
	// private static final String FORM_CONTAINS_ERR_MESSAGE = "form.contains.errors";

	private final TrainTripService tripService;
	private final MessageSource messageSource;

	// Default access constructor that is used to inject the messageSource and the service beans.
	TrainTripController(TrainTripService tripService, MessageSource messageSource) {

		this.tripService = tripService;
		this.messageSource = messageSource;
	}

	/**
	 * Adds a general attribute to the model that is available for all views.
	 * Displays current time stamp.
	 * 
	 * @return String represents the current date time.
	 */
	@ModelAttribute("currentTimeStamp")
	String currentTimeStamp() {
		return new Date().toString();
	}

	/**
	 * Adds a general attribute to the model that is available for all views. Sets
	 * the current active menu as string. Check the header.html file for further
	 * detail.
	 * 
	 * @return String holds he current active menu.
	 */
	@ModelAttribute("module")
	String module() {
		return "trips";
	}

	/**
	 * Renders the HTML view that displays the information of all trips found.
	 * 
	 * @param model         The model that contains the attributes which are
	 *                      required to render the HTML view.
	 * @param requestedWith This is a header parameter for AJAX request with key
	 *                      value pairs X-Requested-With: XMLHttpRequest.
	 * @return The name of the rendered HTML view.
	 */
	@RequestMapping(value = "trip", method = RequestMethod.GET)
	String viewTripList(Model model) {

		// fetch all trips.
		model.addAttribute("trips", tripService.findAll());

		return TRIP_LIST_VIEW_NAME;
	}

	/**
	 * Renders the HTML view that displays the information of the requested trip.
	 * 
	 * @param id    The id of the requested trip.
	 * @param model The model that contains the attributes which are required to
	 *              render the HTML view.
	 * @return The name of the rendered HTML view.
	 */
	@RequestMapping(value = "trip/{id}", method = RequestMethod.GET)
	String viewTripDetail(@PathVariable("id") Integer id, Model model) {

		// fetch trip by trip id.
		TrainTripDto trip = this.getTrip(id);

		// add trip attribute to the model.
		model.addAttribute("trip", trip);

		if (trip.getBookedAisleSeatsCount() + trip.getBookedWindowSeatsCount() == trip.getTotalSeatsCount()) {
			// recommend available trips for the same destination if any exist.
			List<TrainTripDto> trips = (List<TrainTripDto>) tripService.findByDestinationAvailableTrips(trip);
			model.addAttribute("trips", trips);

			if (trips.isEmpty()) {
				model.addAttribute("allAvailableTrips", tripService.findAllAvailableTrips());
			}
		}

		// open the view
		return TRIP_DETAILS_VIEW_NAME;
	}

	/**
	 * Renders the HTML view that displays the view for booking seats for selected
	 * trip.
	 * 
	 * @param model The model that contains the attributes which are required to
	 *              render the HTML view.
	 * @return The name of the rendered HTML view.
	 */
	@RequestMapping(value = "trip/{id}/book", method = RequestMethod.GET)
	String viewBookSeat(@PathVariable("id") Integer id, Model model) {

		// fetch trip by trip id.
		TrainTripDto trip = this.getTrip(id);

		// add attribute to the model.
		model.addAttribute("bookingSeatForm", BookingSeatFormDto.of(trip, trip.getSeats()));
		
		return TRIP_BOOK_SEAT_FORM_VIEW_NAME;
	}

	/**
	 * Books the seat for the selected trip.
	 * 
	 * @param tripDto       The tripDto object that is need to be persisted.
	 * @param result        General interface that represents binding results for
	 *                      error handling purposes.
	 * @param model         The model that contains the attributes which are
	 *                      required to render the HTML view.
	 * @param requestedWith This is a header parameter for AJAX request with key
	 *                      value pairs X-Requested-With: XMLHttpRequest.
	 * @return The name of the rendered HTML view.
	 */
	@RequestMapping(value = "trip/book", method = RequestMethod.POST)
	String bookSeats(@ModelAttribute("bookingSeatForm") BookingSeatFormDto bookingSeatFormDto,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		// book seat(s), persist data to in memory data structure.
		TrainTripDto trainTripDto = bookingSeatFormDto.getTrip();
		List<TrainSeatDto> renderedSeats = tripService.bookSeats(trainTripDto, bookingSeatFormDto.getRenderedSeats())
				.orElseThrow(() -> new TrainSeatAlreadyBookedException(messageSource
						.getMessage("train.seat.already.booked.exception.message", null, Locale.getDefault())));

		// return to the same view, with AJAX request.
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return TRIP_BOOK_SEAT_FORM_VIEW_NAME.concat(" :: ").concat(AJAX_RENDER_BOOK_FORM_FRAGMENT);
		}

		// reflect the changes to the seats booked list, in case of non-AJAX request
		// view rendering.
		bookingSeatFormDto.setRenderedSeats(renderedSeats);

		// return to the view in success path.
		return TRIP_BOOK_SEAT_FORM_VIEW_NAME;
	}

	// Fetches trip by trip id from the trip service.
	private TrainTripDto getTrip(Integer id) throws TrainTripNotFoundException {

		// fetch trip by trip id.
		TrainTripDto trip = tripService.findById(id).orElseThrow(() -> new TrainTripNotFoundException(messageSource
				.getMessage("train.trip.not.found.exception.message", new Object[] { id }, Locale.getDefault())));

		return trip;
	}
}
