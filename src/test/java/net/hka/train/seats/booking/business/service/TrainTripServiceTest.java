/**
 * 
 */
package net.hka.train.seats.booking.business.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import net.hka.train.seats.booking.TestUtil;
import net.hka.train.seats.booking.business.dto.TrainSeatDto;
import net.hka.train.seats.booking.business.dto.TrainTripDto;
import net.hka.train.seats.booking.domain.TrainTrip;

/**
 * Tests all methods in
 * {@link net.hka.train.seats.booking.business.service.TrainTripService} class.
 * 
 * @author Hany Kamal
 */
@DisplayName("When running the test for TrainTripService Class")
@ExtendWith(MockitoExtension.class)
class TrainTripServiceTest {

	private static MessageSource messageSource;

	// @InjectMocks
	private static TrainTripServiceImpl trainTripService;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		trainTripService = new TrainTripServiceImpl(messageSource);
	}

	/**
	 * Test cases for method
	 * {@link net.hka.train.seats.booking.business.service.TrainTripService#findAll()}.
	 */
	@Nested
	@DisplayName("Return the information of all trips")
	class TestFindAll {

		@Nested
		@DisplayName("When no trips are found in CSV file")
		class WhenNoTripsAreFoundInCSV {

			@BeforeEach
			void setDataStoreInService() {
				String csvFileName = TestUtil.createCSVFullFilePath("Train Data_Empty.txt");
				List<TrainTrip> trips = TestUtil.loadTrainTripsFromCSV(csvFileName);
				trainTripService.setDataStore(trips);
			}

			/**
			 * This test ensures that the service will display zero trip if there is no
			 * trips in CSV file.
			 */
			@Test
			@DisplayName("Should display zero trips")
			void shouldDisplayZeroTrips() throws Exception {
				// act
				List<TrainTripDto> tripDtos = trainTripService.findAll();

				// assert
				assertThat(tripDtos, hasSize(0));
			}
		}

		@Nested
		@DisplayName("When two trips are found in the CSV file")
		class WhenTwoTripsAreFound {

			/*
			 * lines 23:23, cHicago, 09 7:15,Portland,24
			 */
			private static final int TRIP_ONE_DEPARTURE_HOUR = 23;
			private static final int TRIP_ONE_DEPARTURE_MINUTE = 23;
			private static final String TRIP_ONE_DESTINATION = "Chicago";
			private static final int TRIP_ONE_TTL_SEATS = 9;

			private static final int TRIP_TWO_DEPARTURE_HOUR = 7;
			private static final int TRIP_TWO_DEPARTURE_MINUTE = 15;
			private static final String TRIP_TWO_DESTINATION = "Portland";
			private static final int TRIP_TWO_TTL_SEATS = 24;

			@BeforeEach
			void setDataStoreInService() {
				String csvFileName = TestUtil.createCSVFullFilePath("Train Data_with_2Trips_records.txt");
				List<TrainTrip> trips = TestUtil.loadTrainTripsFromCSV(csvFileName);
				trainTripService.setDataStore(trips);
			}

			/**
			 * This test ensures that the service will display two trips that are exist.
			 */
			@Test
			@DisplayName("Should display two trips")
			void shouldDisplayTwoTrips() throws Exception {
				// act
				List<TrainTripDto> trips = trainTripService.findAll();

				// assert
				assertThat(trips, hasSize(2));
			}

			/**
			 * These two tests ensure that the service displays the information of the trips
			 * but without checking the correct order for retrieval.
			 */
			@Test
			@DisplayName("Should display the information of the first trip")
			void shouldDisplayInformationOfFirstTrip() throws Exception {
				// act
				List<TrainTripDto> trips = trainTripService.findAll();

				// assert
				assertThat(trips,
						hasItem(allOf(
								hasProperty("departureTime",
										equalTo(LocalTime.of(TRIP_ONE_DEPARTURE_HOUR, TRIP_ONE_DEPARTURE_MINUTE))),
								hasProperty("destination", equalTo(TRIP_ONE_DESTINATION)),
								hasProperty("totalSeatsCount", equalTo(TRIP_ONE_TTL_SEATS)))));
			}

			@Test
			@DisplayName("Should display the information of the second trip")
			void shouldDisplayInformationOfSecondTrip() throws Exception {
				// act
				List<TrainTripDto> trips = trainTripService.findAll();

				// assert
				assertThat(trips,
						hasItem(allOf(
								hasProperty("departureTime",
										equalTo(LocalTime.of(TRIP_TWO_DEPARTURE_HOUR, TRIP_TWO_DEPARTURE_MINUTE))),
								hasProperty("destination", equalTo(TRIP_TWO_DESTINATION)),
								hasProperty("totalSeatsCount", equalTo(TRIP_TWO_TTL_SEATS)))));
			}

			/**
			 * This test ensures that the service displays the information of the trips in
			 * the correct order as in CSV file.
			 */
			@Test
			@DisplayName("Should display the information of the first and second trips in the correct order")
			void shouldDisplayFirstAndSecondTripsInCorrectOrder() throws Exception {
				// act
				List<TrainTripDto> trips = trainTripService.findAll();

				// assert
				assertThat(trips,
						contains(
								allOf(hasProperty("departureTime",
										equalTo(LocalTime.of(TRIP_ONE_DEPARTURE_HOUR, TRIP_ONE_DEPARTURE_MINUTE))),
										hasProperty("destination", equalTo(TRIP_ONE_DESTINATION)),
										hasProperty("totalSeatsCount", equalTo(TRIP_ONE_TTL_SEATS))),
								allOf(hasProperty("departureTime",
										equalTo(LocalTime.of(TRIP_TWO_DEPARTURE_HOUR, TRIP_TWO_DEPARTURE_MINUTE))),
										hasProperty("destination", equalTo(TRIP_TWO_DESTINATION)),
										hasProperty("totalSeatsCount", equalTo(TRIP_TWO_TTL_SEATS)))));
			}
		}
	}

	/**
	 * Test cases for methods
	 * {@link net.hka.train.seats.booking.business.service.TrainTripService#findByDestinationAvailableTrips(net.hka.train.seats.booking.business.dto.TrainTripDto)}
	 * and
	 * {@link net.hka.train.seats.booking.business.service.TrainTripService#findAllAvailableTrips()}.
	 */
	@Nested
	@DisplayName("Return the information of all available trips if the taget trip has no more available seats for booking")
	class TestFindAllAvailableTripsIfTheTargetTripHasNoMoreSeatsAvaialable {

		private TrainTripDto tripWithNoMoreSeats;

		@BeforeEach
		void setDataStoreInService() {
			String csvFileName = TestUtil.createCSVFullFilePath("Train Data_Original.txt");
			List<TrainTrip> trips = TestUtil.loadTrainTripsFromCSV(csvFileName);
			trainTripService.setDataStore(trips);
		}

		private void bookAllSeats(TrainTripDto trip) {
			// mark this trip with no seats available for booking
			List<TrainSeatDto> renderedSeats = trip.getSeats().stream()
					.map(seat -> TrainSeatDto.of(seat.getSeatNumber(), seat.getSeatType(), seat.getLocalizedSeatType()))
					.collect(Collectors.toList());
			trip.getSeats().forEach(seat -> seat.setSeatBooked(Boolean.TRUE));
			trainTripService.bookSeats(trip, renderedSeats);
		}

		/**
		 * Test cases for method
		 * {@link net.hka.train.seats.booking.business.service.TrainTripService#findByDestinationAvailableTrips(net.hka.train.seats.booking.business.dto.TrainTripDto)}
		 */
		@Nested
		@DisplayName("Return the information of all available trips by destination")
		class TestFindAllAvailableTripsByDestination {

			@Nested
			@DisplayName("When no trips are found in CSV file with same destination")
			class WhenNoTripsAreFoundInCSVWithSameDestination {

				private static final int TRIP_ID_WITH_NO_MORE_SEATS = 1;

				@BeforeEach
				void setTargetTrip() {

					Optional<TrainTripDto> optionalTrip = trainTripService.findById(TRIP_ID_WITH_NO_MORE_SEATS);
					if (optionalTrip.isPresent()) {
						tripWithNoMoreSeats = optionalTrip.get();
					}
				}

				/**
				 * This test ensures that the service will display zero trip if there is no
				 * trips in CSV file.
				 */
				@Test
				@DisplayName("Should display zero trips")
				void shouldDisplayZeroTripsWithSameDestination() throws Exception {

					// mark this trip with no seats available for booking
					bookAllSeats(tripWithNoMoreSeats);

					// act
					List<TrainTripDto> tripDtos = (List<TrainTripDto>) trainTripService
							.findByDestinationAvailableTrips(tripWithNoMoreSeats);

					// assert
					assertThat(tripDtos, hasSize(0));
				}
			}

			@Nested
			@DisplayName("When trips are found in CSV file with same destination")
			class WhenTripsAreFoundInCSVWithSameDestination {

				private static final int TRIP_ID_WITH_NO_MORE_SEATS_BUT_THERE_OTHER_TRIP_WITH_SAME_DEST = 4;

				@BeforeEach
				void setTargetTrip() {

					Optional<TrainTripDto> optionalTrip = trainTripService
							.findById(TRIP_ID_WITH_NO_MORE_SEATS_BUT_THERE_OTHER_TRIP_WITH_SAME_DEST);
					if (optionalTrip.isPresent()) {
						tripWithNoMoreSeats = optionalTrip.get();
					}
				}

				/**
				 * This test ensures that the service will display 3 trip should be found in CSV
				 * file.
				 */
				@Test
				@DisplayName("Should display three trip")
				void shouldDisplayThreeTripWithSameDestination() throws Exception {

					// mark this trip with no seats available for booking
					bookAllSeats(tripWithNoMoreSeats);

					// act
					List<TrainTripDto> tripDtos = (List<TrainTripDto>) trainTripService
							.findByDestinationAvailableTrips(tripWithNoMoreSeats);

					// assert
					assertThat(tripDtos, hasSize(3));
				}
			}

		}

		/**
		 * Test cases for method
		 * {@link net.hka.train.seats.booking.business.service.TrainTripService#findAllAvailableTrips()}.
		 */
		@Nested
		@DisplayName("Return the information of all available trips")
		class TestFindAllAvailableTrips {

			private static final int TRIP_ID_WITH_NO_MORE_SEATS = 1;

			@BeforeEach
			void setTargetTrip() {

				Optional<TrainTripDto> optionalTrip = trainTripService.findById(TRIP_ID_WITH_NO_MORE_SEATS);
				if (optionalTrip.isPresent()) {
					tripWithNoMoreSeats = optionalTrip.get();
				}
			}

			/**
			 * This test ensures that the service will display zero trip if there is no
			 * trips in CSV file.
			 */
			@Test
			@DisplayName("Should display eight trips")
			void shouldDisplayEightTripsWithSameDestination() throws Exception {

				// mark this trip with no seats available for booking
				bookAllSeats(tripWithNoMoreSeats);

				// act
				List<TrainTripDto> tripDtos = (List<TrainTripDto>) trainTripService.findAllAvailableTrips();

				// assert
				assertThat(tripDtos, hasSize(8));
			}
		}
	}

	/**
	 * Test cases for method
	 * {@link net.hka.train.seats.booking.business.service.TrainTripService#findById(java.lang.Integer)}.
	 */
	@Nested
	@DisplayName("Return the information of the requested trip")
	class TestFindById {

		private static final int NON_EXIST_TRIP_ID = 0;
		private static final int EXIST_TRIP_ID = 4; // line is: 09.15,San Francisco,6

		@BeforeEach
		void setDataStoreInService() {
			String csvFileName = TestUtil.createCSVFullFilePath("Train Data_Original.txt");
			List<TrainTrip> trips = TestUtil.loadTrainTripsFromCSV(csvFileName);
			trainTripService.setDataStore(trips);
		}

		@Nested
		@DisplayName("When the requested trip isn't found in CSV file")
		class WhenRequestedTripIsNotFound {

			/**
			 * This test ensures that the trip service will return empty optional if the
			 * provided trip id is not exist.
			 */
			@Test
			@DisplayName("Should return empty optional")
			void serviceReturnEmptyOptional() {

				assertEquals(Optional.empty(), trainTripService.findById(NON_EXIST_TRIP_ID));
			}
		}

		@Nested
		@DisplayName("When the requested trip is found in CSV file")
		class WhenRequestedTripIsFound {

			// line is: 09.15,San Francisco,6
			private static final String DEPARTURE_TIME = "09:15";
			private static final String DESTINATION = "San Francisco";
			private static final int TTL_SEATS = 6;

			/**
			 * This test ensures that the service will fetch the exact trip with the same
			 * requested trip id.
			 */
			@Test
			@DisplayName("Should display the information of the correct trip")
			void shouldDisplayInformationOfCorrectTrip() throws Exception {
				// act
				Optional<TrainTripDto> fetchedTrip = trainTripService.findById(EXIST_TRIP_ID);

				// assert
				assertTrue(fetchedTrip.isPresent());
				assertEquals(EXIST_TRIP_ID, fetchedTrip.get().getId());

			}

			/**
			 * This test ensures that the service will fetch the exact trip with the same
			 * attributes.
			 */
			@Test
			@DisplayName("Should display the correct attributes for trip as in CSV file")
			void shouldDisplayCorrectAttributes() throws Exception {
				// act
				Optional<TrainTripDto> fetchedTrip = trainTripService.findById(EXIST_TRIP_ID);

				// assert
				assertTrue(fetchedTrip.isPresent());
				assertEquals(DEPARTURE_TIME, fetchedTrip.get().getDepartureTime().toString());
				assertEquals(DESTINATION, fetchedTrip.get().getDestination());
				assertEquals(TTL_SEATS, fetchedTrip.get().getTotalSeatsCount());
			}
		}
	}

	/**
	 * Test cases for method
	 * {@link net.hka.train.seats.booking.business.service.TrainTripService#bookSeats()}.
	 */
	@Nested
	@DisplayName("Test booking the seats for a selected trip")
	class TestBookSeats {

		private static final int SELECTED_TRIP_ID = 1;
		private static final int SELECTED_BOOKED_SEAT_IDNDEX = 0;
		private TrainTripDto selectedTrip;
		List<TrainSeatDto> renderedTripSeats;

		@BeforeEach
		void setDataStoreInService() {

			String csvFileName = TestUtil.createCSVFullFilePath("Train Data_Original.txt");
			List<TrainTrip> trips = TestUtil.loadTrainTripsFromCSV(csvFileName);
			trainTripService.setDataStore(trips);

			selectedTrip = trainTripService.findById(SELECTED_TRIP_ID).get();

			renderedTripSeats = new ArrayList<>(selectedTrip.getSeats().stream()
					.map(seat -> TrainSeatDto.of(seat.getSeatNumber(), seat.getSeatType(), seat.getLocalizedSeatType()))
					.collect(Collectors.toList()));

			List<TrainSeatDto> selectedTripSeats = selectedTrip.getSeats();
			TrainSeatDto selectedTripSeat = selectedTripSeats.get(SELECTED_BOOKED_SEAT_IDNDEX);
			selectedTripSeat.setSeatBooked(Boolean.TRUE);			
		}

		/**
		 * This test ensures that the service will display zero trip if there is no
		 * trips in CSV file.
		 */
		@Test
		@DisplayName("When booking a seat that is already booked in another session, Should return empty optional")
		void shouldReturnEmptyOptional() throws Exception {
			
			// simulates booking from different session
			trainTripService.bookSeats(selectedTrip, renderedTripSeats);
			
			// act
			Optional<List<TrainSeatDto>> optionalList = trainTripService.bookSeats(selectedTrip, renderedTripSeats);

			// assert
			assertFalse(optionalList.isPresent());
			assertEquals(Optional.empty(), optionalList);
		}
		
		/**
		 * This test ensures that the service will display zero trip if there is no
		 * trips in CSV file.
		 */
		@Test
		@DisplayName("When booking a seat that is already booked in another session, Should return filled optional list")
		void shouldReturnFilledOptionalList() throws Exception {
			
			// act
			Optional<List<TrainSeatDto>> optionalList = trainTripService.bookSeats(selectedTrip, renderedTripSeats);

			// assert
			assertTrue(optionalList.isPresent());
			assertEquals(9, optionalList.get().size());
		}

	}
}