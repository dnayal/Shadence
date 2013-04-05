package controllers;

import handlers.CollectionHandler;
import handlers.EntityPhotoHandler;
import handlers.ExperienceHandler;
import handlers.FeaturedEntityHandler;
import handlers.ProviderExperienceHandler;
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
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import models.Collection;
import models.EntityPhoto;
import models.Experience;
import models.FeaturedEntity;
import models.ProviderExperience;
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
		
		CollectionHandler.createInitialCollectionsForUser(user);
		Util.sendMail(user.getEmail(), "Welcome to Shadence!", views.html.email.userregistration.render(Server.getServerURL()).toString());

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
		
		String userId = Security.decrypt(userIds[0]);
		User user = UserHandler.getUser(userId);
		long createTimestamp = Long.parseLong(Security.decrypt(timestamps[0]));
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
		Integer pageStart = (parameters.get("pageStart")==null)
				?Util.getIntegerProperty("application.pagestart"):Integer.parseInt(Util.getString(parameters.get("pageStart")[0]));

		experienceList = ExperienceHandler.getExperiences(cityId, Util.getString(categoryId), 
													durationLow, durationHigh, priceLow, priceHigh, pageStart);
		
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
			// Set last url here, else JSON string will 
			// be returned if user logs in after using sliders
			Server.setLastUrl();
			List<FeaturedEntity> list = FeaturedEntityHandler.getFeaturedEntitiesForBanner(categoryId);
			return ok(experiences.render(cityId, categoryId, experienceList, list));
		}
		
	}


	public static Result getExperience(String experienceId) {
		Server.setLastUrl();
		
		Experience exp = ExperienceHandler.getExperience(Util.getString(experienceId));
		if(exp==null)
			return redirect(routes.Application.index());
		else
			return ok(experience.render(exp, ExperienceHandler.getSimilarExperiences(experienceId)));
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
		user.setProfilePhotos(oldUser.getProfilePhotos());
		UserHandler.updateUser(user);
		
		MultipartFormData formData = request().body().asMultipartFormData();
		FilePart filePart = formData.getFile("profile_photo");

		if (filePart != null) {

			String supportContentType = Util.getStringProperty("application.photo.type");
			long maxFileSize = Util.getIntegerProperty("application.photo.size");
			long uploadedFilesize = (filePart.getFile().length()/1024/1024);
			String uploadedContentType = filePart.getContentType();
			if((!supportContentType.contains(uploadedContentType)) || (maxFileSize < uploadedFilesize)) {
				Logger.error("Upload file is not supported. File: " + filePart.getFilename() 
						+ " Type: " + uploadedContentType + " Size: " + uploadedFilesize);
				filePart.getFile().delete();
				userForm.reject("Upload file is not supported");
				return badRequest(userprofile.render(user, userForm));
			}
			
			int operation = EntityPhotoHandler.UPDATE_OPERATION;
			List<EntityPhoto> list = user.getProfilePhotos();
			String photoId = null;
			
			if(list==null || list.size()<1)
				operation = EntityPhotoHandler.INSERT_OPERATION;
			else
				photoId = list.get(0).getPhotoId();
			
			EntityPhotoHandler.processEntityPhoto(operation, 
					filePart.getFilename(), filePart.getFile(), photoId,  
					user.getUserId(), EntityPhoto.ENTITY_USER, user.getUserId(), 
					user.getName(),	"1");
		}
		
		return redirect(routes.Application.getUserProfile(user.getUserId()));
	}

	
	public static Result addExperienceToCollection(String experienceId) {
		DynamicForm form = form().bindFromRequest();
		String collectionId = form.field("user_collection").value();
		String newCollectionName = form.field("new_collection").value();
		Collection collection = null;
		
		if(collectionId.equalsIgnoreCase("_new_")) {
			collection = CollectionHandler.saveCollection(Util.getUniqueId(), newCollectionName, "", 
					Server.getCurrentSessionUser(), System.currentTimeMillis());
		} else {
			collection = CollectionHandler.getCollection(collectionId);
		}
		
		CollectionHandler.addExperienceToCollection(collection.getCollectionId(), experienceId);
		
		return redirect(routes.Application.getExperience(experienceId));
		
	}
	
	
	public static Result newCollection(String userId) {
		DynamicForm form = form().bindFromRequest();
		String name = form.field("name").value();
		String description = form.field("description").value();

		CollectionHandler.saveCollection(Util.getUniqueId(), name, description, 
				UserHandler.getUser(userId), System.currentTimeMillis());
		
		return redirect(routes.Application.showCollectionsOfUser(userId));
	}
	
	
	public static Result removeExperienceFromCollection(String collectionId, String experienceId) {
		Collection collection = CollectionHandler.getCollection(collectionId);
		if (UserHandler.isUserProfileOwner(collection.getUser().getUserId()))
			CollectionHandler.removeExperienceFromCollection(collectionId, experienceId);
		
		return redirect(routes.Application.showCollection(collectionId));
	}
	

	public static Result showCollectionsOfUser(String userId) {
		Server.setLastUrl();
		List<Collection> collections = CollectionHandler.getCollectionsOfUser(userId);	
		return ok(usercollections.render(UserHandler.getUser(userId), collections));
	}
	
	
	public static Result showCollection(String collectionId) {
		Server.setLastUrl();
		Collection collection = CollectionHandler.getCollection(collectionId);
		return ok(usercollection.render(collection.getUser(), collection));
	}
	
	
	public static Result updateCollection(String collectionId) {
		DynamicForm form = form().bindFromRequest();
		String name = form.field("name").value();
		String description = form.field("description").value();
		Collection collection = CollectionHandler.getCollection(collectionId);
		if (UserHandler.isUserProfileOwner(collection.getUser().getUserId())) {
			collection.setName(name);
			collection.setDescription(description);
			CollectionHandler.updateCollection(collection);
		}
		
		return redirect(routes.Application.showCollection(collectionId));
	}
	
	
	public static Result deleteCollection(String collectionId) {
		Collection collection = CollectionHandler.getCollection(collectionId);
		if (UserHandler.isUserProfileOwner(collection.getUser().getUserId()))
			CollectionHandler.deleteCollection(collectionId);
		
		return redirect(routes.Application.showCollectionsOfUser(collection.getUser().getUserId()));
	}
	
	
	public static Result moveExperienceFromCollection(String collectionId) {
		DynamicForm form = form().bindFromRequest();
		String targetCollectionId = form.field("user_collection").value();
		String experienceToMove = form.field("user_experience").value();
		Collection collection = CollectionHandler.getCollection(collectionId);
		if (UserHandler.isUserProfileOwner(collection.getUser().getUserId())) {
			CollectionHandler.removeExperienceFromCollection(collectionId, experienceToMove);
			CollectionHandler.addExperienceToCollection(targetCollectionId, experienceToMove);
		}
		
		return redirect(routes.Application.showCollection(collectionId));
	}
	
	
	public static Result viewProviderExperienceForm() {
		return ok(submitexperience.render(form(ProviderExperience.class)));
	}
	
	
	public static Result thankProviderSubmission(){
		return ok(thanksforlocalsubmission.render());
	}
	
	
	public static Result submitProviderExperience() {
		Form<ProviderExperience> experienceForm = form(ProviderExperience.class).bindFromRequest();
		ProviderExperience providerExperience = experienceForm.get();
		providerExperience.setCreateTimestamp(System.currentTimeMillis());
		providerExperience.setId(Util.getUniqueId());
		ProviderExperienceHandler.saveProviderExperience(providerExperience);
		Util.sendMail("info@shadence.com", "New Experience Submitted #" + providerExperience.getId(), views.html.email.newexperience_notification.render(providerExperience).toString());
		return redirect(routes.Application.thankProviderSubmission());
	}
}