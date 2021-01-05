package net.hka.train.seats.booking.web.controller;

//import static net.hka.train.seats.booking.config.WebTestConfig.exceptionResolver;
import static net.hka.train.seats.booking.config.WebTestConfig.fixedLocaleResolver;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.hka.train.seats.booking.business.dto.TrainTripDto;
import net.hka.train.seats.booking.business.exception.TrainTripNotFoundException;
import net.hka.train.seats.booking.business.service.TrainTripService;
import net.hka.train.seats.booking.web.exception.handler.TrainTripNotFoundHandler;

/**
 * Tests all methods in
 * {@link net.hka.train.seats.booking.web.controller.TrainTripController} class.
 * 
 * @author Hany Kamal
 */
class TrainTripControllerTest {
	
	// Views names and relative path.
	private static final String TRIP_LIST_VIEW_NAME = "trip/trip-list";
	private static final String TRIP_DETAILS_VIEW_NAME = "trip/trip-details";
	private static final String TRIP_BOOK_SEAT_FORM_VIEW_NAME = "trip/book-form";
	
	private static final String VIEW_NAME_ERROR_VIEW = "error/general";
	
    private TrainTripRequestBuilder requestBuilder;
    private TrainTripService service;
    private MessageSource messageSource;
    
    @BeforeEach
    void configureSystemUnderTest() {
        service = mock(TrainTripService.class);
        
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TrainTripController(service, messageSource))
                .setControllerAdvice(new Object[] {new TrainTripNotFoundHandler()})
                //.setHandlerExceptionResolvers(exceptionResolver())
                .setLocaleResolver(fixedLocaleResolver())
                //.setViewResolvers(jspViewResolver())
                .build();
        requestBuilder = new TrainTripRequestBuilder(mockMvc);
    }
    
    @Nested
    @DisplayName("Render the HTML view that displays the information of all trips")
    class TestTrips {

    	/**
         * This test ensures that the status code is 200.
         */
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            requestBuilder.viewTripList().andExpect(status().isOk());
        }

        /**
         * This test ensures that the trip list view is the view that is rendered.
         */
        @Test
        @DisplayName("Should render the trip list view")
        void shouldRenderTripListView() throws Exception {
            requestBuilder.viewTripList().andExpect(view().name(TRIP_LIST_VIEW_NAME));
        }
        
        @Nested
        @DisplayName("When no trips are found in CSV file")
        class WhenNoTripsAreFound {

            @BeforeEach
            void serviceReturnsEmptyList() {
                when(service.findAll()).thenReturn(new ArrayList<>());
            }

            /**
             * This test ensures that the list view displays zero trips 
             * if no trip presents in the CSV file.
             */
            @Test
            @DisplayName("Should display zero trips")
            void shouldDisplayZeroTrips() throws Exception {
                requestBuilder.viewTripList().andExpect(model().attribute("trips", hasSize(0)));
            }
        }

        @Nested
        @DisplayName("When two trips are found")
        class WhenTwoTripsAreFound {

            private static final int TRIP_ONE_DEPARTURE_HOUR = 12;
            private static final int TRIP_ONE_DEPARTURE_MINUTE = 15;
            private static final String TRIP_ONE_DEST = "Chicago";
			private static final int TRIP_ONE_TTL_SEATS = 12;
            
			private static final int TRIP_TWO_DEPARTURE_HOUR = 10;
            private static final int TRIP_TWO_DEPARTURE_MINUTE = 30;
            private static final String TRIP_TWO_DEST = "New York";
			private static final int TRIP_TWO_TTL_SEATS = 30;

            @BeforeEach
            void serviceReturnsTwoTrips() {
            	TrainTripDto firstDto = new TrainTripDto(); 
            	firstDto.setDepartureTime(LocalTime.of(TRIP_ONE_DEPARTURE_HOUR, TRIP_ONE_DEPARTURE_MINUTE));
            	firstDto.setDestination(TRIP_ONE_DEST);
				firstDto.setTotalSeatsCount(TRIP_ONE_TTL_SEATS);
				
				TrainTripDto secondDto = new TrainTripDto();
				secondDto.setDepartureTime(LocalTime.of(TRIP_TWO_DEPARTURE_HOUR, TRIP_TWO_DEPARTURE_MINUTE));
				secondDto.setDestination(TRIP_TWO_DEST);
				secondDto.setTotalSeatsCount(TRIP_TWO_TTL_SEATS);
                
                when(service.findAll()).thenReturn(Arrays.asList(firstDto, secondDto));
            }

            /**
             * This test ensures that the list view displays the information
             * of the exact 2 items.
             */
            @Test
            @DisplayName("Should display two trips")
            void shouldDisplayTwoTrips() throws Exception {
                requestBuilder.viewTripList().andExpect(model().attribute("trips", hasSize(2)));
            }

            /**
             * These two tests ensure that the list view displays the information
             * of the found trips but they don't guarantee that trips
             * are displayed in the correct order.
             */
            @Test
            @DisplayName("Should display the information of the first trip")
            void shouldDisplayInformationOfFirstTrip() throws Exception {
            	requestBuilder.viewTripList()
                        .andExpect(
                                model().attribute("trips",
                                hasItem(allOf(
                                		hasProperty("departureTime",
        										equalTo(LocalTime.of(TRIP_ONE_DEPARTURE_HOUR, TRIP_ONE_DEPARTURE_MINUTE))),
        								hasProperty("destination", equalTo(TRIP_ONE_DEST)),
        								hasProperty("totalSeatsCount", equalTo(TRIP_ONE_TTL_SEATS))
                                )))
                        );
            }

            @Test
            @DisplayName("Should display the information of the second trip")
            void shouldDisplayInformationOfSecondTrip() throws Exception {
                requestBuilder.viewTripList()
                        .andExpect(
                                model().attribute("trips",
                                        hasItem(allOf(
                                        		hasProperty("departureTime",
                										equalTo(LocalTime.of(TRIP_TWO_DEPARTURE_HOUR, TRIP_TWO_DEPARTURE_MINUTE))),
                								hasProperty("destination", equalTo(TRIP_TWO_DEST)),
                								hasProperty("totalSeatsCount", equalTo(TRIP_TWO_TTL_SEATS))
                                        )))
                        );
            }

            /**
             * This test ensures that the list view displays the information
             * of the found trips in the correct order.
             */
            @Test
            @DisplayName("Should display the information of the first and second trip in the correct order")
            void shouldDisplayFirstAndSecondTripInCorrectOrder() throws Exception {
                requestBuilder.viewTripList()
                        .andExpect(
                                model().attribute("trips",
                                		contains(
                                                allOf(
                                                		hasProperty("departureTime",
                        										equalTo(LocalTime.of(TRIP_ONE_DEPARTURE_HOUR, TRIP_ONE_DEPARTURE_MINUTE))),
                        								hasProperty("destination", equalTo(TRIP_ONE_DEST)),
                        								hasProperty("totalSeatsCount", equalTo(TRIP_ONE_TTL_SEATS))
                                                ),
                                                allOf(
                                                		hasProperty("departureTime",
                        										equalTo(LocalTime.of(TRIP_TWO_DEPARTURE_HOUR, TRIP_TWO_DEPARTURE_MINUTE))),
                        								hasProperty("destination", equalTo(TRIP_TWO_DEST)),
                        								hasProperty("totalSeatsCount", equalTo(TRIP_TWO_TTL_SEATS))
                                                )
                                        ))
                        );
            }
        }
    }
    
    @Nested
    @DisplayName("Render the HTML view that displays the information of the requested trip")
    class TestViewTripDetail {

        private static final int TRIP_ID = 300;

        @Nested
        @DisplayName("When the requested trip isn't found")
        class WhenRequestedTripIsNotFound {

            @BeforeEach
            void serviceReturnThrowException() {
            	when(service.findById(Mockito.anyInt())).thenThrow(new TrainTripNotFoundException());
            }

            /**
             * This test ensures that the status code is 404
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.viewTripDetail(TRIP_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the general error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the the general error view")
            void shouldRender404View() throws Exception {
                requestBuilder.viewTripDetail(TRIP_ID).andExpect(view().name(VIEW_NAME_ERROR_VIEW));
            }
        }
        
        @Nested
        @DisplayName("When the requested trip is found")
        class WhenRequestedTripIsFound {
        	
        	private static final int DEPARTURE_HOUR = 23;
			private static final int DEPARTURE_MINUTE = 23;
			private static final String DESTINATION = "Chicago";
			private static final int TTL_SEATS = 9;
                       
            @BeforeEach
            void serviceReturnsTrip() {
        		TrainTripDto dto = new TrainTripDto();
        		dto.setDepartureTime(LocalTime.of(DEPARTURE_HOUR, DEPARTURE_MINUTE));
            	dto.setDestination(DESTINATION);
            	dto.setTotalSeatsCount(TTL_SEATS);
        		when(service.findById(Mockito.anyInt())).thenReturn(Optional.of(dto));
            }

            /**
             * This test ensures that the status code is 200
             */
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                requestBuilder.viewTripDetail(TRIP_ID).andExpect(status().isOk());
            }

            /**
             * This test ensures that the trip is the view that is rendered
             */
            @Test
            @DisplayName("Should render the view trip detail")
            void shouldRenderViewTripView() throws Exception {
            	requestBuilder.viewTripDetail(TRIP_ID).andExpect(view().name(TRIP_DETAILS_VIEW_NAME));
            }
           
            /**
             * This test ensures that the trip detail view displays the information
             * of the selected trip.
             */
            @Test
            @DisplayName("Should display the correct data for selected trip")
            void shouldDisplayCorrectData() throws Exception {
                requestBuilder.viewTripDetail(TRIP_ID)
                        .andExpect(model().attribute(
                                "trip",
                                allOf(
                                		hasProperty("departureTime",
        										equalTo(LocalTime.of(DEPARTURE_HOUR, DEPARTURE_MINUTE))),
        								hasProperty("destination", equalTo(DESTINATION)),
        								hasProperty("totalSeatsCount", equalTo(TTL_SEATS))
                                )
                        ));
            }
        }
    }    

    @Nested
    @DisplayName("Render the HTML view that displays the list of seats for the requested trip for booking purpose")
    class viewBookSeat {

        private static final int TRIP_ID = 300;

        @Nested
        @DisplayName("When the requested trip isn't found")
        class WhenRequestedTripIsNotFound {
        	        
            @BeforeEach
            void serviceReturnThrowException() {
            	when(service.findById(Mockito.anyInt())).thenThrow(new TrainTripNotFoundException());
            }

            /**
             * This test ensures that the status code is 404
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.viewBookSeat(TRIP_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the general error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the the general error view")
            void shouldRender404View() throws Exception {
                requestBuilder.viewBookSeat(TRIP_ID).andExpect(view().name(VIEW_NAME_ERROR_VIEW));
            }
        }
        
        @Nested
        @DisplayName("When the requested trip is found")
        class WhenRequestedTripIsFound {
        	
        	private TrainTripDto mockedTrip;
        	
        	private static final int DEPARTURE_HOUR = 23;
			private static final int DEPARTURE_MINUTE = 23;
			private static final String DESTINATION = "Chicago";
			private static final int TTL_SEATS = 9;
                       
            @BeforeEach
            void serviceReturnsTrip() {
            	
            	mockedTrip = new TrainTripDto();
            	mockedTrip = mock(TrainTripDto.class);
            	
        		//TrainTripDto dto = new TrainTripDto();
            	mockedTrip.setDepartureTime(LocalTime.of(DEPARTURE_HOUR, DEPARTURE_MINUTE));
            	mockedTrip.setDestination(DESTINATION);
            	mockedTrip.setTotalSeatsCount(TTL_SEATS);
            	mockedTrip.setSeats(new ArrayList<>());
        		when(service.findById(Mockito.anyInt())).thenReturn(Optional.of(mockedTrip));
            }

            /**
             * This test ensures that the status code is 200
             */
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                requestBuilder.viewBookSeat(TRIP_ID).andExpect(status().isOk());
            }

            /**
             * This test ensures that the trip is the view that is rendered
             */
            @Test
            @DisplayName("Should render the view trip detail")
            void shouldRenderViewTripView() throws Exception {
            	requestBuilder.viewBookSeat(TRIP_ID).andExpect(view().name(TRIP_BOOK_SEAT_FORM_VIEW_NAME));
            }
           
            /**
             * This test ensures that the trip detail view displays the information
             * of the selected trip.
             */
            @Test
            @DisplayName("Should display the correct data for selected trip")
            void shouldDisplayCorrectData() throws Exception {
                requestBuilder.viewBookSeat(TRIP_ID)
                        .andExpect(
                        		model()
	                        		.attribute("bookingSeatForm", 
	                        				hasProperty("trip"))
                        		);
            }

        }
    }    
}
