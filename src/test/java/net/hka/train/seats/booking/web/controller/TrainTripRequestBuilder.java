package net.hka.train.seats.booking.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import net.hka.train.seats.booking.business.dto.BookingSeatFormDto;

/**
 * Creates and sends the HTTP requests which are used to write unit tests for
 * controllers methods which provide CRUD operations for train trip.
 */
class TrainTripRequestBuilder {

	private final MockMvc mockMvc;

	TrainTripRequestBuilder(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	/**
	 * Creates and sends the HTTP requests which gets the HTML document that
	 * displays the information of all trips.
	 * 
	 * @return {@link ResultActions}
	 * @throws {@link Exception}
	 */
	ResultActions viewTripList() throws Exception {
		return mockMvc.perform(get("/trip"));
	}

	/**
	 * Creates and sends the HTTP request which gets the HTML document that displays
	 * the information of the requested trip.
	 * 
	 * @param id The id of the requested trip.
	 * @return {@link ResultActions}
	 * @throws {@link Exception}
	 */
	ResultActions viewTripDetail(Integer id) throws Exception {
		return mockMvc.perform(get("/trip/{id}", id));
	}

	/**
	 * Creates and sends the HTTP request which gets the HTML document that displays
	 * the information of the requested trip for booking seats purpose.
	 * 
	 * @param id The id of the requested trip.
	 * @return {@link ResultActions}
	 * @throws {@link Exception}
	 */
	ResultActions viewBookSeat(Integer id) throws Exception {
		return mockMvc.perform(get("/trip/{id}/book", id));
	}

	/**
	 * Creates and sends the HTTP post request which sets the HTML document that
	 * displays the result after posting the current task to be saved or updated in
	 * the database.
	 * 
	 * @param bookingSeatFormDto The DTO that is binded to the booking view.
	 * @return {@link ResultActions}
	 * @throws {@link Exception}
	 */
	ResultActions bookSeats(BookingSeatFormDto bookingSeatFormDto) throws Exception {
    	return mockMvc.perform(post("trip/book")
    			.param("trip", bookingSeatFormDto.getTrip().toString())
    			.param("renderedSeats", bookingSeatFormDto.getRenderedSeats().toString())
    			.header("X-Requested-With", ""));
    }
}
