# Railway Station Online Booking System
This system handles a seat booking operation in a Railway stations. It loads a data from the provided "Train Data.txt" CSV file that contains the details of trains departing from a train station, to provide the following features:
 
	1) Lists all trains displaying destination and departure time.
	2) Enables a train to be selected and a window or aisle seat to be booked.
	3) In the event a desired seat (window/aisle) is not available, offers the option of booking another available seat.
	4) If no seats are available, offers the option of picking a different train to a different destination.

With the following assumptions:

	1) The fields in the text file are: Departure Time, Destination, total number of seats.
	2) The total number of seats can be assumed to be an equal split between window and aisle. For example, 12 total means 6 window and 6 aisle seats.

## Technologies used
	1- Spring Boot. 
	2- Thymeleaf with Spring MVC.
	3- Thymeleaf with AJAX support through Thymeleaf fragments to send AJAX requests from views to the controller.
	4- Thymeleaf layout features that allows to share the repeated parts in presentation layer among multiple views. 
	5- Using Bootstrap, and JQuery within Thymeleaf. 

#### Following is a sample code from "trip.js" file for using AJAX request in booking seat scenario:		
```javascript
	$.fn.doBookSeat = function() {
		var $form = $('#bookTripForm');
		var $successMessage = $('#successMessageDiv');
		//$(document.body).append($form);
		
		// declare submit action for the form
		$form.on('submit', function(e) {
	    	e.preventDefault();
	    	$.ajax({
	    		url: $form.attr('action'),
	    		type: 'post',
	    		data: $form.serialize(),
	    		success: function(response) {    	
	    			// replace the form
	    			$form.replaceWith(response);
	    			
	    			// if the response contains any errors, hide message part
	    			//if ($(response).find('.has-error').length > 0) {
	    			if(response.indexOf('alert') > -1) {
	    				// hide the success message
	    				$successMessage.addClass('hidden');
	    				$successMessage.hide();
	    			} else {
	    				// redirect to trip list
	    				//var pathname = window.location.pathname;
	    				//pathname = pathname.substring(0, pathname.lastIndexOf('/')+1);
	    				//window.location.replace(pathname + "trip");

	    				// show the success message
	    				$successMessage.removeClass('hidden');
	    				$successMessage.show();	    				
	    			}
	    		}
	    	});
	    });
		
		$form.submit();
	};  
```

## IDE used
This project is Java-based Maven project developed using Spring Tool Suite 4 (STS). STS is an Eclipse-based development environment that is customized for the development of Spring applications and mainly for Sping Boot applications.

## Database used
Although no database is used, the design of the application is built in a way to allow easily consideration for using a database if needed.

## Main features
* JQuery is used to handle AJAX requests with Spring MVC.

* Junit Jupiter is used to test the service and the MVC controller.

* DTO (Data Transfer Object) design pattern is used, so the mapping between DTO and Domain Entity objects are done using Java 8 features.
	* Note: With more DTO and domain objects are used by a certain application, there are a different ways that are available to make the mapping operation much easier such as using an external API.	

* Localization: Spring framework provides LocalResolver to handle internationalization and localization, and this application utilizes this feature from Spring framwork to be localized enabled system introducing "messages.properties" under resources directory and used its values in the presentation layer.

## Main Interfaces, Classes and Exceptions
* TrainTrip - Represents the domain entity for the train trip and it holds the data that will be loaded and assigned from CSV file.
* DepartureTimeConverter - A custom converter to convert the departure time string to a "LocalTime" java object.
* DestinationConverter - A custom converter to convert the destination string words into capitalized words, such that the first character in each word is made up of upper case character and then a series of lower case characters.
* IntegerGreaterThanZeroValidator - A final class validator extends MustMatchRegexExpression from Opencsv API that validates if the provided number string is matching a valid integer value format with value that is greater than Zero.
* Time24HoursValidator - A final class validator extends MustMatchRegexExpression from openCSV API that validates if the provided time is matching a 24 time format.
* CsvDataProvider - An interface which provides all necessary methods that are used to load CSV files and binding the data to the domain entity list.
* CsvIOException - RuntimeException that is thrown by a CsvDataProvider if an error is occurred while reading from the CSV file.
* CsvNoDataFoundException - RuntimeException is thrown by a CsvDataProvider if there is no data found in the CSV file.
* CsvParsingException - Extends CsvException from Opencsv API and is thrown by a CsvDataProvider if an error is occurred while parsing the CSV file at binding to the domain entity phase.

* TrainTripDto - A POJO data transfer object for summarizing the train trip data model. It is used also to hide the implementation details of train trip domain entity.
* TrainSeatDto - A POJO data transfer object for summarizing the train seat data model.
* BookingSeatFormDto - A POJO data transfer object for summarizing the booking seat view data model.
* TrainTripService - An interface which provides all necessary methods and data structures that are required for handling the train trip details.
* TrainTripController - The end point MVC controller that manages all the requests for handling the train trip operations.
* TrainSeatAlreadyBookedException - RuntimeException is thrown by a TripBookingController if no more seats are available for booking.
* TrainTripNotFoundException - RuntimeException is thrown by a TripBookingController if the trip is not found.
* DataInitializer - Runs at the loading time of the application to load data from the CSV file using CsvDataProvider, and then populates/sets the data store that is handled by TrainTripService.

## Data structures used
A List is used to represent a list of available train trips with the following considerations: 
* The read and write operations on this list is done through the TrainTripServiceImpl class.

* Since this list is marked as static and shared among all instances of this class, it's declared as a Thread-safe using a synchronizedList method in java.util.Collections utility class.

## Design patterns and design decisions used
* By using Spring framework, there are many design patterns that are available and ready for use. Following are design patterns or standard practices that are applied in this application:
	* Dependency injection or inversion of control (IOC): ...
	* Factory Design Pattern: ...
	* Proxy Design Pattern: ...
	* Model View Controller (MVC): ...
	* Front Controller Design Pattern: ...
	* View Helper: ...	

	In addition to DTO design pattern.

* “java.lang.Enum” represents a reference name for seat types, to acheive the following benefits:
  1. Compile time safety against entering an invalid seat type.
  2. Reusable and well encapsulated.

* This project uses an external API called "Opencsv". It is an easy-to-use CSV (comma-separated values) parser library for Java. Java 8 is currently the minimum supported version in this API. It has many features, some of them are as follows:

	Opencsv supports all the basic CSV-type things you’re likely to want to do:
	* Arbitrary numbers of values per line.
	* Ignoring commas in quoted elements.
	* Handling quoted entries with embedded carriage returns (i.e. entries that span multiple lines).
	* Configurable separator and quote characters (or use sensible defaults).

	All of these things can be done reading and writing, using a manifest of malleable methodologies:
	* To and from an array of strings.
	* To and from annotated beans.
	* From a database
	* Read all the entries at once, or use an Iterator-style model.

	Additionally Opencsv supports internationalization for all error messages it produces.

## Design Assumptions
* All seats with odd numbers are of type window seats and all seats with even numbers are of type aisle.
* No empty lines are allowed in the provided CSV file and a CsvParsingException will be thrown from the application at loading time in case of an empty line is detected.
* CSV file provides the departure time in 24-Hours format.
* The departure time that is provided in CSV file is provided with '.' or ':' as a separator between hours and minutes so the possible time formats in CSF file are as follows:
		H:mm, H.mm, HH:mm, and HH.mm
* All trips are booked for future datetime.

## Reference and Documentation
* Opencsv
		http://opencsv.sourceforge.net/

* What & Why DTO
		https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis

* Spring il18n
		https://developer.okta.com/blog/2019/02/25/java-i18n-internationalization-localization
		https://lokalise.com/blog/spring-boot-internationalization/

* Spring Formatting Techniques
		https://docs.spring.io/spring-framework/docs/3.0.0.RC1/reference/html/ch05s06.html

* Icons with character
		https://medium.com/@iris.atlanttida/square-symbol-fb68b812a621

* Ajax with Thymeleaf Blog
		https://coding-ajax-with-thymeleaf.blogspot.com/
	
* For further ducumentation for this application, please consider the documents folder under the project root folder: 
	* UML class diagram (MainClassDIagram.gif or MainClassDIagram.ucls).
	* Generated JavaDoc.