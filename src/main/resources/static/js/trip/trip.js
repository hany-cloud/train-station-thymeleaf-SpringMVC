//console.log("Trip");

$(function() {
	
	// handle click event for save task button 	
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
});