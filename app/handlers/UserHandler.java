package handlers;

import java.util.List;

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
	 * Saves the user
	 */
	public static void saveUser(User user) {
		user.save();
	}
	

	/**
	 * Saves the user
	 */
	public static User saveUser(String name, String email, String password, String gender) {

		String userId = Util.getUniqueId();
		User user = new User(userId, name, email, password, gender, System.currentTimeMillis());
		
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
	 * Returns all users
	 */
	public static List<User> getUsers() {
		return User.find.all();
	}
	
	
}
