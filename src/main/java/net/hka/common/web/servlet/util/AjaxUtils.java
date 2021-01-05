package net.hka.common.web.servlet.util;

/**
 * <p>
 * A utility helper class used to check if the @RequestHeader with key such as
 * "X-Requested-With" has a value "XMLHttpRequest".
 * </p>
 * <p>
 * For example in any of the MCV controller method you can receive a parameter
 * like @RequestHeader(value = "X-Requested-With", required = false) String
 * requestedWith, then in the body of that method you can call the method in
 * this class to check if this request parameter is an AJAX request as follows:
 * if (AjaxUtils.isAjaxRequest(requestedWith)) { .... }
 * </p>
 * 
 * @author Hany Kamal
 */
public class AjaxUtils {

	private AjaxUtils() {
	}

	/**
	 * Check if the request parameter is an AJAX request.
	 * 
	 * @param requestedWith The request header parameter with key such as
	 *                      "X-Requested-With".
	 * @return Boolean indicating that the header request parameter has value equal
	 *         to "XMLHttpRequest".
	 */
	public static boolean isAjaxRequest(final String requestedWith) {

		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}
}
