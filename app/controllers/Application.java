package controllers;

import handlers.ExperienceHandler;
import handlers.UserHandler;
import handlers.VenueHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import exceptions.DuplicateEntityException;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import models.Experience;
import models.User;
import models.Venue;

import utils.Security;
import utils.Server;
import utils.Util;
import views.html.*;

public class Application extends Controller {
	
	
	public static Result index() {
		return redirect(routes.Application.getExperiences(Util.getStringProperty("city.default"), Util.getStringProperty("category.default")));
	}
	
	
	public static Result aboutShadence() {
		return ok(about.render());
	}
	

	public static Result viewLoginRegister() {
		return ok(loginregister.render(form(User.class)));
	}
	

	public static Result login() {
		Form<User> userForm = form(User.class).bindFromRequest();
		if (userForm.hasErrors()) {
			userForm.reject("login_error", "Incorrect email id or password");
			return badRequest(loginregister.render(userForm));
		}

		User userObject = userForm.get();
		User user = UserHandler.getUserByEmailAndPassword(userObject.getEmail(), userObject.getPassword());
		if (user == null) {
			userForm.reject("login_error", "Incorrect email id or password");
			return badRequest(loginregister.render(userForm));
		} else {
			Boolean rememberMe = new Boolean(userForm.field(Server.REMEMBER_ME).value());
			if (rememberMe)
				Server.rememberUser(user);
			Server.setCurrentSessionUser(user.getUserId());
			return redirect(Server.getLastUrl());
		}
	}
	

	public static Result logout() {
		session().clear();
		Server.forgetUser();
		return redirect(routes.Application.index());
	}
	
		
	public static Result register() {
		Form<User> userForm = form(User.class).bindFromRequest();
		if (userForm.hasErrors()) {
			userForm.reject("register_error", "Invalid information entered");
			return badRequest(loginregister.render(userForm));
		}
		User user = userForm.get();
		String userId = Util.getUniqueId();
		user.setUserId(userId);
		user.setPassword(Security.generateHash(userId, user.getPassword()));
		user.setCreateTimestamp(System.currentTimeMillis());
		
		try {
			UserHandler.saveUser(user);
		} catch (DuplicateEntityException exception) {
			userForm.reject("register_error", "User already registered with this email id");
			return badRequest(loginregister.render(userForm));
		}
		
		Boolean rememberMe = new Boolean(userForm.field(Server.REMEMBER_ME).value());
		if (rememberMe)
			Server.rememberUser(user);
		Server.setCurrentSessionUser(user.getUserId());
		return redirect(Server.getLastUrl());
	}
	

	public static Result viewForgotPassword() {
		Map<String, String[]> map = request().queryString();
		String type[] = map.get(Server.PASSWORD_REQUEST_TYPE);
		if (type == null || type[0].equalsIgnoreCase(Server.FORGOT_PASSWORD))
			return ok(forgotpassword.render(true));
		
		String userIds[] = map.get(Security.PARAM_USERID);
		String timestamps[] = map.get(Security.PARAM_TIMESTAMP);
		if (userIds==null || timestamps==null)
			return ok(forgotpassword.render(false));
		
		String userId = userIds[0];
		User user = UserHandler.getUser(userId);
		long createTimestamp = Long.parseLong(timestamps[0]);
		long timeDifference = (System.currentTimeMillis() - createTimestamp)/(1000*60*60);
		
		if (timeDifference>=24 || user == null)
			return ok(forgotpassword.render(false));

		Server.setCurrentSessionUser(user.getUserId());
		return ok(forgotpassword.render(true));
	}
	
	
	public static Result forgotPassword() {
		DynamicForm form = form().bindFromRequest();
		String requestType = form.field(Server.PASSWORD_REQUEST_TYPE).value();
		
		if(requestType.equalsIgnoreCase(Server.FORGOT_PASSWORD)) {
			UserHandler.forgotPassword(form.field("email").value());
			return redirect(routes.Application.viewLoginRegister());
			
		} else if (requestType.equalsIgnoreCase(Server.RESET_PASSWORD)) {
			String password = form.field("password").value();
			if (!UserHandler.changePassword(Server.getCurrentSessionUser(), password)) {
				Logger.error("Unable to change password", new RuntimeException("Unable to change password"));
				return redirect(routes.Application.forgotPassword().toString() + "?" 
								+ Server.PASSWORD_REQUEST_TYPE + "=" + Server.RESET_PASSWORD);
			}
		}
		return redirect(routes.Application.index());
	}
	
	
	public static Result getExperiences(String cityId, String categoryId) {
		Server.setLastUrl();
		
		List<Experience> experienceList = new ArrayList<Experience>();
		
		// Refer Experience model for min and max values of priceRating
		// Minimum duration for any experience is 1 hour, 
		// max duration has been set to 10000 (more than a year) in order to cover all scenarios
		Map<String, String[]> parameters = request().queryString();
		String priceLow = (parameters.get("priceLow")==null)
				?Util.getStringProperty("priceRating.min"):Util.getString(parameters.get("priceLow")[0]);
		String priceHigh = (parameters.get("priceHigh")==null)
				?Util.getStringProperty("priceRating.max"):Util.getString(parameters.get("priceHigh")[0]);
		String durationLow = (parameters.get("durationLow")==null)
				?Util.getStringProperty("duration.min"):Util.getString(parameters.get("durationLow")[0]);
		String durationHigh = (parameters.get("durationHigh")==null)
				?Util.getStringProperty("duration.max"):Util.getString(parameters.get("durationHigh")[0]);
		
		experienceList = ExperienceHandler.getExperiences(cityId, Util.getString(categoryId), 
													durationLow, durationHigh, priceLow, priceHigh);
		
		String format = (parameters.get("format")==null)?"":Util.getString(parameters.get("format")[0]);
		
		if(format.equalsIgnoreCase(Util.getStringProperty("format.json"))) {
			ObjectMapper mapper = new ObjectMapper();
			StringBuffer json = new StringBuffer();
			try {
				json.append(mapper.writeValueAsString(experienceList));
			} catch (Exception exception) {
				Logger.error("Error while creating JSON string.", exception);
			}
			return ok(json.toString());
		} else {
			return ok(experiences.render(cityId, categoryId, experienceList));
		}
		
	}


	public static Result getExperience(String experienceId) {
		Server.setLastUrl();
		
		Experience exp = ExperienceHandler.getExperience(Util.getString(experienceId));
		if(exp==null)
			return redirect(routes.Application.index());
		else
			return ok(experience.render(exp));
	}
	
	
	public static Result getVenue(String venueId) {
		Server.setLastUrl();
		
		Venue v = VenueHandler.getVenue(Util.getString(venueId));
		
		if (v==null)
			return redirect(routes.Application.index());
		else
			return ok(venue.render(v, ExperienceHandler.getExperiencesAtVenue(venueId)));
	}
	
	
	public static Result getUserProfile(String userId) {
		User user = UserHandler.getUser(userId);
		Form<User> userForm = form(User.class).fill(user);
		return ok(userprofile.render(user, userForm));
	}
	
	
	public static Result updateProfile(String userId) {
		Form<User> userForm = form(User.class).bindFromRequest();
		User oldUser = UserHandler.getUser(userId);
		if(userForm.hasErrors()) {
			userForm.reject("Error while processing your request");
			return badRequest(userprofile.render(oldUser, userForm));
		}
		
		if (!UserHandler.isUserProfileOwner(userId)) {
			userForm.reject("Invalid Request");
			return badRequest(userprofile.render(oldUser, userForm));
		}
		
		User user = userForm.get();
		user.setUserId(oldUser.getUserId());
		user.setCreateTimestamp(oldUser.getCreateTimestamp());
		if (user.getPassword().length() < 1)
			user.setPassword(oldUser.getPassword());
		else
			user.setPassword(Security.generateHash(user.getUserId(), user.getPassword()));
		user.setRoles(oldUser.getRoles());
		UserHandler.updateUser(user);
		
		return redirect(routes.Application.getUserProfile(user.getUserId()));
	}

	
	/**
	 * This action allows photos folder to be moved outside 
	 * Play installation and serving dynamically uploaded images 
	 */
	public static Result getImage(String filename) {
		  return ok(new java.io.File(Util.getStringProperty("photos.upload.path") + "/" + filename) );
	}
}