package net.hka.train.seats.booking.business.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.text.WordUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import net.hka.train.seats.booking.business.dto.TrainSeatDto;
import net.hka.train.seats.booking.business.dto.TrainSeatType;
import net.hka.train.seats.booking.business.dto.TrainTripDto;
import net.hka.train.seats.booking.domain.TrainTrip;

/**
 * Concrete implementation for
 * {@link net.hka.train.seats.booking.business.service.TrainTripService}
 * interface.
 * 
 * @author Hany Kamal
 */
@Service("TrainTripService")
class TrainTripServiceImpl implements TrainTripService {

	// Reference for a message source bean
	private final MessageSource messageSource;

	// A formatting pattern for displaying the departure time for the trip in 12
	// hour mode.
	private final String time12HourPattern;

	// Holds all trips loaded from CSV file, acting as a data store (i.e a
	// database).
	private static List<TrainTripDto> syncTripsCollection;
	
	/**
	 * Default access constructor used to inject the {@link MessageSource} bean and
	 * initialize some instance and static class variables.
	 * 
	 * @param messageSource
	 */
	TrainTripServiceImpl(MessageSource messageSource) {

		this.messageSource = messageSource;

		time12HourPattern = messageSource != null
				? messageSource.getMessage("time.12.hour.pattern", null, Locale.getDefault())
				: DEFAULT_TIME_12HOUR_PATTERN;

		// initialize our data store.
		syncTripsCollection = Collections.synchronizedList(new ArrayList<>());
	}

	@Override
	public void setDataStore(Iterable<TrainTrip> trips) {

		if (trips == null)
			throw new IllegalArgumentException("The paremter is null");

		if (syncTripsCollection != null && syncTripsCollection.isEmpty()) {
			
			// iterates over the train trip entity list to map it to a train trip DTO list.
			List<TrainTripDto> tripDtos = StreamSupport.stream(trips.spliterator(), false)
				.map(trip -> TrainTripDto.of(trip.getId(), trip.getDepartureTime(),
						trip.getDepartureTime().toString(), trip.getDestination(), trip.getTotalSeatsCount(),
						new ArrayList<>()))
				.collect(Collectors.toList());
			
			if(!tripDtos.isEmpty()) syncTripsCollection = Collections.synchronizedList(tripDtos);
		}
			

		// iterate over the trips to populate seats for each trip.
		syncTripsCollection.stream().map(trip -> {
			List<TrainSeatDto> seats = initializeTrainSeats(trip.getTotalSeatsCount());
			trip.setSeats(seats);

			String localizedDepartureTime = trip.getDepartureTime()
					.format(DateTimeFormatter.ofPattern(time12HourPattern));
			trip.setLocalizedDepartureTime(localizedDepartureTime);

			return trip;
		}).collect(Collectors.toList());
	}

	@Override
	public Optional<List<TrainSeatDto>> bookSeats(final TrainTripDto tripDto, final List<TrainSeatDto> renderedSeats) {

		if (tripDto == null)
			throw new IllegalArgumentException("The \"tripDto\" paremter is null");
		
		if (renderedSeats == null)
			throw new IllegalArgumentException("The \"renderedSeats\" paremter is null");

		// get the trip by id.
		TrainTripDto persistedTrip = getById(tripDto.getId());

		// check non of the seats are already booked.
		for (int i = 0; i < tripDto.getSeats().size(); i++) {

			TrainSeatDto formSeat = tripDto.getSeats().get(i);
			TrainSeatDto persistedSeat = persistedTrip.getSeats().get(i);
			TrainSeatDto onRenderSeat = renderedSeats.get(i);
			if (formSeat.isSeatBooked() && !onRenderSeat.isSeatBooked()) { // means new checked seat from the current view session

				if (persistedSeat.isSeatBooked()) { // means this seat is booked from different session 
					
					// data not saved or persisted.
					return Optional.empty();
				} else {
					
					// mark seat as booked.
					persistedSeat.setSeatBooked(Boolean.TRUE);

					// update window and aisle seats counts.
					maintainBookedSeatCounts(persistedTrip);
				}
			}
		}

		// return a new mapped list of seats, Since no reference is allowed for the list
		// that is holding trips.
		return Optional.of(persistedTrip.getSeats().stream().map(seatCloneMapper).collect(Collectors.toList()));
		// return Optional.of(new ArrayList<>(persistedTrip.getSeats()));
	}

	@Override
	public List<TrainTripDto> findAll() {

		return syncTripsCollection.stream()
				.map(tripCloneMapper)
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<TrainTripDto> findByDestinationAvailableTrips(final TrainTripDto tripDto) {

		if (tripDto == null)
			throw new IllegalArgumentException("The paremter is null");
		
		// filter available trips for the same destination with available seats for
		// booking.
		List<TrainTripDto> recommendedTrips = syncTripsCollection.stream()
				.filter(trip -> trip.getDestination().equalsIgnoreCase(tripDto.getDestination()) // same destination.
						/* && trip.getDepartureTime().isAfter(tripDto.getDepartureTime()) */ // commented based on the
																								// assumption: all trips
																								// are booked for future
																								// date-time.
						&& trip.hasSeatAvailable() // trip is still has available seats to be booked.
				).collect(Collectors.toList());

		return recommendedTrips.stream()
				.map(tripCloneMapper)
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<TrainTripDto> findAllAvailableTrips() {
		
		// filter all trips with available seats for booking.
		List<TrainTripDto> recommendedTrips = syncTripsCollection.stream()
				.filter(trip -> trip.hasSeatAvailable()) // trip is still has available seats to be booked.
				.collect(Collectors.toList());
		
		return recommendedTrips.stream()
				.map(tripCloneMapper)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<TrainTripDto> findById(final Integer id) {

		if (id == null)
			throw new IllegalArgumentException("The paremter is null");

		try {

			// get the trip by id.
			TrainTripDto trainTripDto = getById(id);
			
			return Optional.of(trainTripDto).map(tripCloneMapper);

		} catch (IndexOutOfBoundsException e) {

			return Optional.empty();
		}
	}

	// Initialize the list of seats for the trip.
	private List<TrainSeatDto> initializeTrainSeats(final int totalSeatsCount) {

		List<TrainSeatDto> seats = new ArrayList<>(totalSeatsCount);
		for (int seatNumber = 1; seatNumber < totalSeatsCount + 1; seatNumber++) {

			// total number of seats are split equally between window and aisle types.
			TrainSeatType seatType = (seatNumber % 2 == 0) ? TrainSeatType.AISLE : TrainSeatType.WINDOW;
			TrainSeatDto seat = TrainSeatDto.of(seatNumber, seatType, getLocalizedValue(seatType));
			seats.add(seat);
		}

		return seats;
	}

	// Returns the localized value using the enumeration value of the provided seat
	// type.
	private String getLocalizedValue(final TrainSeatType seatType) {
		return messageSource != null
				? messageSource.getMessage("seat.type.".concat(seatType.toString().toLowerCase()), null,
						Locale.getDefault())
				: WordUtils.capitalizeFully(seatType.toString().toLowerCase());
	}

	// Return an instance of trip using the provided trip id, directly from the data
	// store (the list).
	private TrainTripDto getById(final Integer id) {
		return syncTripsCollection.get(id - 1);
	}

	// Update window and aisle seats count in the trip.
	private void maintainBookedSeatCounts(final TrainTripDto tripDto) {
		long bookedWindowSeatsCount = tripDto.getSeats().stream()
				.filter(seat -> seat.isSeatBooked() && seat.getSeatType() == TrainSeatType.WINDOW).count();
		tripDto.setBookedWindowSeatsCount((int) bookedWindowSeatsCount);

		long bookedAisleSeatsCount = tripDto.getSeats().stream()
				.filter(seat -> seat.isSeatBooked() && seat.getSeatType() == TrainSeatType.AISLE).count();
		tripDto.setBookedAisleSeatsCount((int) bookedAisleSeatsCount);
	}

	// Maps seat object to another instance performing a deep copy, Since no
	// reference is allowed for the list that is holding trips.
	private Function<TrainSeatDto, TrainSeatDto> seatCloneMapper = this::seatClone;

	private TrainSeatDto seatClone(TrainSeatDto seat) {

		TrainSeatDto newSeat = TrainSeatDto.of(seat.getSeatNumber(), seat.getSeatType(), seat.getLocalizedSeatType());
		newSeat.setSeatBooked(seat.isSeatBooked());
		return newSeat;
	}
	
	
	// Maps trip object to another instance performing a deep copy, Since no
	// reference is allowed for the list that is holding trips.
	private Function<TrainTripDto, TrainTripDto> tripCloneMapper = this::tripClone;
		
	private TrainTripDto tripClone(TrainTripDto trainTripDto) {
		List<TrainSeatDto> seats = trainTripDto.getSeats().stream().map(seatCloneMapper).collect(Collectors.toList()); // copy the seat list
		TrainTripDto returnedTrainTripDto = TrainTripDto.of(trainTripDto.getId(), trainTripDto.getDepartureTime(),
				trainTripDto.getDepartureTime().format(DateTimeFormatter.ofPattern(time12HourPattern)),
				trainTripDto.getDestination(), trainTripDto.getTotalSeatsCount(), seats);			
		returnedTrainTripDto.setBookedWindowSeatsCount(trainTripDto.getBookedWindowSeatsCount()); // set the window seat count
		returnedTrainTripDto.setBookedAisleSeatsCount(trainTripDto.getBookedAisleSeatsCount());
		
		return returnedTrainTripDto;
	}
}
