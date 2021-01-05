package net.hka.train.seats.booking.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * The end point controller that controls all the requests for landing home page.
 * 
 * @author Hany Kamal
 */
@Controller
class HomeController {

	/**
	 * Adds a general attribute to the model that is available for all views. Sets
	 * the current active menu as string. Check the header.html file for further
	 * detail.
	 * 
	 * @return String holds he current active menu.
	 */
    @ModelAttribute("module")
    String module() {
        return "home";
    }

    /**
     * Renders the HTML view that displays the information landing home view.
     * 
     * @return The name of the rendered HTML view.
     */
    @GetMapping("/")
    String index() {    	
        return "home/home";
    }
}
