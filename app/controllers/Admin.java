package controllers;

import java.util.Map;

import models.City;
import models.EntityPhoto;
import models.Experience;
import models.ExperienceCategory;
import models.User;
import models.Venue;
import handlers.CityHandler;
import handlers.EntityPhotoHandler;
import handlers.ExperienceCategoryHandler;
import handlers.ExperienceHandler;
import handlers.UserHandler;
import handlers.VenueHandler;
import play.Logger;
import play.Play;
import play.Configuration;
import play.data.Form;
import play.mvc.Http.MultipartFormData;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import utils.Util;
import views.html.admin.*;

public class Admin extends Controller {
	
	static Configuration config = Play.application().configuration();
	
	public static Result getMain() {
		Logger.info("+-+-+-+-+-+-+-+");
		Logger.info(String.valueOf(Play.isProd()));
		Logger.info(config.getString("application.name"));
		Logger.info("+-+-+-+-+-+-+-+");
		return ok(main_admin.render(CityHandler.getCities()));
	}

	
/********************
 * EXPERIENCE - START
 ********************/
	public static Result getExperiences(String cityId) {
		return ok(experiences.render(cityId, ExperienceHandler.getAllExperiences(cityId)));
	}
	

	public static Result showExperienceForm(String cityId, String experienceId) {
		Form<Experience> experienceForm = null;
		if (Util.getString(experienceId).equalsIgnoreCase("new")) {
			experienceForm = form(Experience.class);
		} else {
			experienceForm = form(Experience.class).fill(ExperienceHandler.getExperience(experienceId));
		}

		return ok(experiences_form.render(cityId, experienceForm, VenueHandler.getVenuesMap(cityId), 
				ExperienceCategoryHandler.getExperienceCategoriesMap()));
	}
	
	
	public static Result saveExperience(String cityId) {
		Form<Experience> experienceForm = form(Experience.class).bindFromRequest();
		String experienceId = null;
		if(experienceForm.hasErrors())
			return badRequest(experiences_form.render(cityId, experienceForm, 
					VenueHandler.getVenuesMap(cityId), ExperienceCategoryHandler.getExperienceCategoriesMap()));
		else {
			MultipartFormData body = request().body().asMultipartFormData();
			Experience experience = experienceForm.get();
			if (Util.getString(experience.getExperienceId()).equalsIgnoreCase("")) {
				experienceId = Util.getUniqueId();
				experience.setExperienceId(experienceId);
				experience.setCreateTimestamp(System.currentTimeMillis());
				ExperienceHandler.saveExperience(experience);
				
			} else {
				
				experienceId = experience.getExperienceId();
				experience.setCreateTimestamp(System.currentTimeMillis());
				ExperienceHandler.updateExperience(experience);
				// update photos of the experience
				for (EntityPhoto entityPhoto : experience.getExperiencePhotos()) {
					FilePart filePart = body.getFile(entityPhoto.getPhotoId());
					if(filePart != null) {
						EntityPhotoHandler.updateEntityPhoto(filePart.getFilename(), filePart.getFile(), 
								entityPhoto.getPhotoId(), experienceId, EntityPhoto.ENTITY_EXPERIENCE, 
								null, entityPhoto.getAlternateText(), entityPhoto.getPhotoOrder());
					} else {
						EntityPhoto oldPhoto = EntityPhotoHandler.getEntityPhoto(entityPhoto.getPhotoId());
						oldPhoto.setAlternateText(entityPhoto.getAlternateText());
						oldPhoto.setPhotoOrder(entityPhoto.getPhotoOrder());
						EntityPhotoHandler.updateEntityPhotoinDatabase(oldPhoto);
					}
				}
			}

			// save new photos of the experience
			// this is applicable to both new and existing experiences
			Map<String, String> photoMap = EntityPhotoHandler.getPhotoMap(experienceId, EntityPhoto.ENTITY_EXPERIENCE);
			for(FilePart photo : body.getFiles() ) {
				if(!photoMap.containsKey(photo.getKey())) {
					EntityPhotoHandler.saveEntityPhoto(photo.getFilename(), photo.getFile(), 
							experienceId, EntityPhoto.ENTITY_EXPERIENCE, null, 
							experienceForm.field(photo.getKey().concat("_alternate_text")).value(), 
							experienceForm.field(photo.getKey().concat("_photo_order")).value());
				}
			}

			return redirect(routes.Admin.getExperiences(cityId));
			
		}
	}
/******************
 * EXPERIENCE - END
 ******************/
	
	
/***************
 * VENUE - START
 ***************/
	public static Result getVenues(String cityId) {
		return ok(venues.render(cityId, VenueHandler.getVenues(cityId)));
	}
	

	public static Result showVenueForm(String cityId, String venueId) {
		Form<Venue> venueForm = null;
		if (Util.getString(venueId).equalsIgnoreCase("new"))
			venueForm = form(Venue.class);
		else
			venueForm = form(Venue.class).fill(VenueHandler.getVenue(venueId));

		return ok(venues_form.render(cityId, venueForm, CityHandler.getCitiesMap()));
	}
	
	
	public static Result saveVenue(String cityId) {
		Form<Venue> venueForm = form(Venue.class).bindFromRequest();
		if(venueForm.hasErrors())
			return badRequest(venues_form.render(cityId, venueForm, CityHandler.getCitiesMap()));
		else {
			Venue venue = venueForm.get();
			if (Util.getString(venue.getVenueId()).equalsIgnoreCase("")) {
				venue.setVenueId(Util.getUniqueId());
				venue.setCreateTimestamp(System.currentTimeMillis());
				VenueHandler.saveVenue(venue);
			} else {
				venue.setCreateTimestamp(System.currentTimeMillis());
				VenueHandler.updateVenue(venue);
			}

			return redirect(routes.Admin.getVenues(cityId));
			
		}
	}
/*************
 * VENUE - END
 *************/
	

/****************************
 * EXPERIENCECATEGORY - START
 ****************************/
	public static Result getCategories() {
		return ok(categories.render(ExperienceCategoryHandler.getExperienceCategories()));
	}
	

	public static Result showCategoryForm(String categoryId) {
		Form<ExperienceCategory> categoryForm = null;
		if (Util.getString(categoryId).equalsIgnoreCase("new"))
			categoryForm = form(ExperienceCategory.class);
		else
			categoryForm = form(ExperienceCategory.class).fill(ExperienceCategoryHandler.getExperienceCategory(categoryId));

		return ok(categories_form.render(categoryForm));
	}
	
	
	public static Result saveCategory() {
		Form<ExperienceCategory> categoryForm = form(ExperienceCategory.class).bindFromRequest();
		if(categoryForm.hasErrors())
			return badRequest(categories_form.render(categoryForm));
		else {
			ExperienceCategory category = categoryForm.get();
			ExperienceCategory fromDB = ExperienceCategoryHandler.getExperienceCategory(category.getCategoryId());
			if (fromDB == null) {
				category.setCreateTimestamp(System.currentTimeMillis());
				ExperienceCategoryHandler.saveExperienceCategory(category);
			} else {
				category.setCreateTimestamp(System.currentTimeMillis());
				ExperienceCategoryHandler.updateExperienceCategory(category);
			}

			return redirect(routes.Admin.getCategories());
			
		}
	}
/**************************
 * EXPERIENCECATEGORY - END
 **************************/

	
/**************
 * CITY - START
 **************/
	public static Result getCities() {
		return ok(cities.render(CityHandler.getCities()));
	}
	

	public static Result showCityForm(String cityId) {
		Form<City> cityForm = null;
		if (Util.getString(cityId).equalsIgnoreCase("new"))
			cityForm = form(City.class);
		else
			cityForm = form(City.class).fill(CityHandler.getCity(cityId));

		return ok(cities_form.render(cityForm));
	}
	
	
	public static Result saveCity() {
		Form<City> cityForm = form(City.class).bindFromRequest();
		if(cityForm.hasErrors())
			return badRequest(cities_form.render(cityForm));
		else {
			MultipartFormData body = request().body().asMultipartFormData();
			City city = cityForm.get();
			City fromDB = CityHandler.getCity(city.getCityId());
			if (fromDB == null) {
				city.setCreateTimestamp(System.currentTimeMillis());
				CityHandler.saveCity(city);
			} else {
				city.setCreateTimestamp(System.currentTimeMillis());
				CityHandler.updateCity(city);
				// update photos of city
				for (EntityPhoto entityPhoto : city.getFlagPhotos()) {
					FilePart filePart = body.getFile(entityPhoto.getPhotoId());
					if(filePart != null) {
						EntityPhotoHandler.updateEntityPhoto(filePart.getFilename(), filePart.getFile(), 
								entityPhoto.getPhotoId(), city.getCityId(), EntityPhoto.ENTITY_CITY, 
								null, entityPhoto.getAlternateText(), entityPhoto.getPhotoOrder());
					} else {
						EntityPhoto oldPhoto = EntityPhotoHandler.getEntityPhoto(entityPhoto.getPhotoId());
						oldPhoto.setAlternateText(entityPhoto.getAlternateText());
						oldPhoto.setPhotoOrder(entityPhoto.getPhotoOrder());
						EntityPhotoHandler.updateEntityPhotoinDatabase(oldPhoto);
					}
				}
				
			}
			
			// save new photos of the city
			// this is applicable to both new and existing city
			Map<String, String> photoMap = EntityPhotoHandler.getPhotoMap(city.getCityId(), EntityPhoto.ENTITY_CITY);
			for(FilePart photo : body.getFiles() ) {
				if(!photoMap.containsKey(photo.getKey())) {
					EntityPhotoHandler.saveEntityPhoto(photo.getFilename(), photo.getFile(), 
							city.getCityId(), EntityPhoto.ENTITY_CITY, null, 
							cityForm.field(photo.getKey().concat("_alternate_text")).value(), 
							cityForm.field(photo.getKey().concat("_photo_order")).value());
				}
			}

			return redirect(routes.Admin.getCities());
			
		}
	}
/************
 * CITY - END
 ************/


/**************
 * USER - START
 **************/
	public static Result getUsers() {
		return ok(users.render(UserHandler.getUsers()));
	}
	

	public static Result showUserForm(String userId) {
		Form<User> userForm = null;
		if (Util.getString(userId).equalsIgnoreCase("new"))
			userForm = form(User.class);
		else
			userForm = form(User.class).fill(UserHandler.getUser(userId));

		return ok(users_form.render(userForm));
	}
	
	
	public static Result saveUser() {
		Form<User> userForm = form(User.class).bindFromRequest();
		if(userForm.hasErrors())
			return badRequest(users_form.render(userForm));
		else {
			User user = userForm.get();
			MultipartFormData body = request().body().asMultipartFormData();
			String userId = null;
			if (Util.getString(user.getUserId()).equalsIgnoreCase("")) {
				userId = Util.getUniqueId();
				user.setUserId(userId);
				user.setCreateTimestamp(System.currentTimeMillis());
				UserHandler.saveUser(user);
			} else {
				userId = user.getUserId();
				user.setCreateTimestamp(System.currentTimeMillis());
				UserHandler.updateUser(user);
				// update photos of user
				for (EntityPhoto entityPhoto : user.getProfilePhotos()) {
					FilePart filePart = body.getFile(entityPhoto.getPhotoId());
					if(filePart != null) {
						EntityPhotoHandler.updateEntityPhoto(filePart.getFilename(), filePart.getFile(), 
								entityPhoto.getPhotoId(), userId, EntityPhoto.ENTITY_USER, 
								null, entityPhoto.getAlternateText(), entityPhoto.getPhotoOrder());
					} else {
						EntityPhoto oldPhoto = EntityPhotoHandler.getEntityPhoto(entityPhoto.getPhotoId());
						oldPhoto.setAlternateText(entityPhoto.getAlternateText());
						oldPhoto.setPhotoOrder(entityPhoto.getPhotoOrder());
						EntityPhotoHandler.updateEntityPhotoinDatabase(oldPhoto);
					}
				}
			}

			// save new photos of the users
			// this is applicable to both new and existing users
			Map<String, String> photoMap = EntityPhotoHandler.getPhotoMap(userId, EntityPhoto.ENTITY_USER);
			for(FilePart photo : body.getFiles() ) {
				if(!photoMap.containsKey(photo.getKey())) {
					EntityPhotoHandler.saveEntityPhoto(photo.getFilename(), photo.getFile(), 
							userId, EntityPhoto.ENTITY_USER, null, 
							userForm.field(photo.getKey().concat("_alternate_text")).value(), 
							userForm.field(photo.getKey().concat("_photo_order")).value());
				}
			}

			return redirect(routes.Admin.getUsers());
		}
	}
/************
 * USER - END
 ************/
	
}
