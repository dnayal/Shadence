package handlers;

import java.util.Date;
import java.util.List;

import exceptions.DuplicateEntityException;

import utils.Security;
import utils.Server;
import utils.Util;
import models.User;

public class UserHandler {

	
	/**
	 * Updates the user
	 */
	public static void updateUser(User user) {
		user.update();
	}

	
	/**
	 * Saves the user. If a user is already registered with 
	 * the same email id, an error is thrown
	 */
	public static void saveUser(User user) {
		User userObject = User.find.where().eq("email", user.getEmail()).findUnique();
		if (userObject == null) {
			user.save();
		} else
			throw new DuplicateEntityException("Duplicate email id");
	}
	

	/**
	 * Saves the user
	 */
	public static User saveUser(String name, String email, String password, String gender, 
								Date birthdate, String city, String country, String roles) {

		String userId = Util.getUniqueId();
		User user = new User(userId, name, email, password, gender, birthdate, 
										city, country, roles, System.currentTimeMillis());
		
		saveUser(user);
		
		return user;
	}
	
	
	/**
	 * Returns a user based on id
	 */
	public static User getUser(String userId) {
		return User.find.byId(Util.getString(userId));
	}
	

	/**
	 * Returns a user based on email id
	 */
	public static User getUserByEmail(String email) {
		return User.find.where().eq("email", email).findUnique();
	}
	

	/**
	 * Returns a user based on email id and confirms it using password
	 * Used for user login
	 */
	public static User getUserByEmailAndPassword(String email, String password) {
		User user = getUserByEmail(email);
		if (user == null)
			return null;
		else
			if (Security.generateHash(user.getUserId(), password).equalsIgnoreCase(user.getPassword()))
				return user;
			else 
				return null;
	}
	

	/**
	 * Returns all users
	 */
	public static List<User> getUsers() {
		return User.find.all();
	}
	
	
	/**
	 * Based on the email initiates the forgot password request of the user
	 */
	public static void forgotPassword(String email) {
		User user = getUserByEmail(email);
		if (user == null)
			return;

		String url = Server.getForgotPasswordServerURL() 
						+ "?" + Server.PASSWORD_REQUEST_TYPE + "=" + Server.RESET_PASSWORD  
						+ "&" + Security.PARAM_USERID + "=" + user.getUserId()
						+ "&" + Security.PARAM_TIMESTAMP + "=" + String.valueOf(System.currentTimeMillis());
		
		Util.sendMail(email, "Shadence Password Reset Request", views.html.email.forgotpassword.render(url).toString());
	}
	

	/**
	 * Changes password of the given user
	 */
	public static Boolean changePassword(User user, String password) {
		if (user == null)
			return false;
		
		user.setPassword(Security.generateHash(user.getUserId(), password));
		user.update();
		return true;
	}
	

	/**
	 * Checks whether the profile is being accessed by the owner
	 */
	public static Boolean isUserProfileOwner(String userId) {
		String sessionUserId = Server.getCurrentSessionUserId();
		
		if(sessionUserId==null || (!sessionUserId.equalsIgnoreCase(userId)))
			return false;
		
		return true;
	}

}
