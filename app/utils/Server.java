package utils;

import controllers.routes;
import handlers.UserHandler;
import models.User;
import play.mvc.Controller;
import play.mvc.Http.Cookie;

public class Server extends Controller {
	
	private static final String USER_ID = "user_id";
	private static final String LAST_URL = "last_url";

	public static final String REMEMBER_ME = "remember_me";
	public static final String PASSWORD_REQUEST_TYPE = "type";
	public static final String FORGOT_PASSWORD = "forgot";
	public static final String RESET_PASSWORD = "reset";

	
	/**
	 * Sets the last URL browsed by user
	 * Required to redirect user to that original page 
	 * he was browsing after registration or login
	 */
	public static void setLastUrl() {
		session(LAST_URL, request().uri());
	}

	
	/**
	 * Returns the last URL browsed by user
	 * Required to redirect user to that original page 
	 * he was browsing after registration or login
	 */
	public static String getLastUrl() {
		if (session(LAST_URL) == null)
			return routes.Application.index().toString();
		else
			return session(LAST_URL);
	}
	
	
	/**
	 * Returns true if any user is logged in
	 */
	public static boolean isUserLoggedIn() {
		String userId = session(USER_ID);
		User user = UserHandler.getUser(userId);
		if (user == null)
			return false;
		else
			return true;
	}
	
	
	/**
	 * Returns the details of currently loggedin user
	 */
	public static User getCurrentSessionUser() {
		String userId = session(USER_ID);
		return UserHandler.getUser(userId);
	}

	
	/**
	 * Returns the userId of currently loggedin user
	 */
	public static String getCurrentSessionUserId() {
		return session(USER_ID);
	}

	
	/**
	 * Sets the user id for the session
	 */
	public static void setCurrentSessionUser(String userId) {
		session(USER_ID, userId);
	}


	/**
	 * Remembers the user for 180 days
	 */
	public static void rememberUser(User user) {
		response().setCookie(REMEMBER_ME, Security.encrypt(user.getUserId()), 60*60*24*180);
	}
	
	
	/**
	 * Remembers the user for 180 days
	 */
	public static boolean isUserRemembered() {
		Cookie cookie = request().cookies().get(REMEMBER_ME);
		if (cookie == null)
			return false;
		String userId = Security.decrypt(cookie.value());
		User user = UserHandler.getUser(userId);
		if(user == null)
			return false;
		else {
			// TODO check if we want to create session for remembered users 
			setCurrentSessionUser(user.getUserId());
			return true;
		}
	}
	
	
	/**
	 * Deletes the remember me cookie
	 */
	public static void forgetUser() {
		response().discardCookies(REMEMBER_ME);
	}
	
	
	public static boolean isValidUser() {
		return  (isUserLoggedIn() | isUserRemembered());
	}
	
	
	public static Boolean isResetPasswordRequest(){
		try {
			return String.valueOf(request().queryString().get(PASSWORD_REQUEST_TYPE)[0])
					.equalsIgnoreCase(RESET_PASSWORD);
		} catch(Exception exception) {
			return false;
		}
	}
	
	
	public static String getForgotPasswordServerURL() {
		return getServerURL() + routes.Application.viewForgotPassword();
	}


	public static String getServerURL() {
		String url = routes.Application.index().absoluteURL(request());
		if(url.endsWith("/"))
			url = url.substring(0, url.length()-1);
		return url;
	}
	
}
